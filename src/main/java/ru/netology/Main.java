package ru.netology;

public class Main {
        public static void main(String[] args) {
            final Server server = new Server(9999);

            server.addHandler("GET", "/messages", (request, responseStream) -> {
                // Обработка GET-запроса
                // responseStream.write(...);
            });

            server.addHandler("POST", "/messages", (request, responseStream) -> {
                // Обработка POST-запроса
                // responseStream.write(...);
            });

            server.listen();
        }
    }