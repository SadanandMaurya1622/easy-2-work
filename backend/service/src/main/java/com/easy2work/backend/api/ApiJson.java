package com.easy2work.backend.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class ApiJson {

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private ApiJson() {
    }

    public static void write(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");
        resp.getWriter().write(GSON.toJson(body));
    }

    public static void writeError(HttpServletResponse resp, int status, String message) throws IOException {
        write(resp, status, Map.of("ok", false, "error", message));
    }
}
