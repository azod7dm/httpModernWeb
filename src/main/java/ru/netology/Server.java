package ru.netology;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        // Реализация чтения запроса (как текст запроса),
        // создание и возврат объекта Request
        return new Request(); // Заглушка
    }
}
