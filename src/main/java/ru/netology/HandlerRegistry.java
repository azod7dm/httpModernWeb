package ru.netology;

import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry {
    private final Map<String, Map<String, Handler>> handlers = new HashMap<>();

    public void addHandler(String method, String path, Handler handler) {handlers.computeIfAbsent(method, k -> new HashMap<>()).put(path, handler);
    }

    public Handler getHandler(String method, String path) {
        return handlers.getOrDefault(method, new HashMap<>()).get(path);
    }
}
