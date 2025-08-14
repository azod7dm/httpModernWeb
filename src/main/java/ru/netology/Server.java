package ru.netology;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Server {
    private final int port;
    private final ExecutorService threadPool;
    private final HandlerRegistry handlerRegistry; // Регистрация обработчиков

    public Server(int port) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(64);
        this.handlerRegistry = new HandlerRegistry();
    }

    public void addHandler(String method, String path, Handler handler) {
        handlerRegistry.addHandler(method, path, handler);
    }

    public void listen() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> handleConnection(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection(Socket clientSocket) {
        try (BufferedOutputStream responseStream = new BufferedOutputStream(clientSocket.getOutputStream())) {
            // Чтение запроса, создание объекта Request
            Request request = readRequest(clientSocket); // Этот метод нужно реализовать
            Handler handler = handlerRegistry.getHandler(request.getMethod(), request.getPath());
            if (handler != null) {
                handler.handle(request, responseStream);
            } else {
                // Обработка ошибки 404
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Реализация метода для чтения HTTP-запроса и создания объекта Request
    private Request readRequest(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                return null; // Пустой запрос
            }

            // Обработаем первую строку запроса
            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String pathWithQuery = parts[1];
            String protocol = parts[2];

            String body = ""; // Хранит тело запроса

            Map<String, String> headers = new HashMap<>();
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                String[] headerParts = line.split(": ");
                if (headerParts.length == 2) {
                    headers.put(headerParts[0], headerParts[1]);
                }
            }

            // Если метод POST, читаем тело запроса
            if (method.equals("POST")) {
                body = reader.lines().collect(Collectors.joining("\n"));
            }

            // Создаем объект Request
            return new Request(method, pathWithQuery, headers, body);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Ошибка при чтении запроса
        }
    }
}
