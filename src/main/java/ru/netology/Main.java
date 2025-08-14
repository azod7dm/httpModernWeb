package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        final Server server = new Server(9999);

        server.addHandler("GET", "/messages", (request, responseStream) -> {
            String lastParam = request.getQueryParam("last");
            String responseBody = "GET /messages called with last parameter: " + lastParam;
            sendResponse(responseStream, 200, responseBody);
        });

        server.addHandler("POST", "/messages", (request, responseStream) -> {
            String body = request.getBody();
            String responseBody = "POST /messages called with body: " + body;
            sendResponse(responseStream, 201, responseBody);
        });

        server.listen();
    }

    private static void sendResponse(BufferedOutputStream responseStream, int statusCode, String responseBody) {
        try {
            String statusLine = "HTTP/1.1 " + statusCode + " " + getStatusMessage(statusCode) + "\r\n";
            String headers = "Content-Type: text/plain; charset=UTF-8\r\n" +
                    "Content-Length: " + responseBody.length() + "\r\n" +
                    "\r\n";
            responseStream.write(statusLine.getBytes(StandardCharsets.UTF_8));
            responseStream.write(headers.getBytes(StandardCharsets.UTF_8));
            responseStream.write(responseBody.getBytes(StandardCharsets.UTF_8));
            responseStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getStatusMessage(int statusCode) {
        switch (statusCode) {
            case 200: return "OK";
            case 201: return "Created";
            case 400: return "Bad Request";
            case 404: return "Not Found";
            default: return "Internal Server Error";
        }
    }
}
