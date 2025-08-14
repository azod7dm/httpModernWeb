package ru.netology;

import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Request {
    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final String body;
    private final Map<String, String> queryParams; // Новое поле для параметров

    public Request(String method, String path, Map<String, String> headers, String body) {
        this.method = method;
        this.path = path;
        this.headers = headers != null ? new HashMap<>(headers) : new HashMap<>();
        this.body = body;
        this.queryParams = parseQueryParams(path); // Парсинг параметров из пути
    }

    private Map<String, String> parseQueryParams(String path) {
        String queryString = "";
        if (path.contains("?")) {
            String[] pathParts = path.split("\\?");
            path = pathParts[0]; // Путь
            queryString = pathParts[1]; // Параметры
        }
        return URLEncodedUtils.parse(queryString, StandardCharsets.UTF_8).stream()
                .collect(Collectors.toMap(
                        org.apache.http.NameValuePair::getName,
                        org.apache.http.NameValuePair::getValue,
                        (oldValue, newValue) -> newValue)); // Если параметр повторяется, берем последнее значение
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    // Новый метод для получения параметра по имени
    public String getQueryParam(String name) {
        return queryParams.get(name);
    }

    // Новый метод для получения всех параметров
    public Map<String, String> getQueryParams() {
        return new HashMap<>(queryParams); // Возвращаем копию параметров
    }
}
