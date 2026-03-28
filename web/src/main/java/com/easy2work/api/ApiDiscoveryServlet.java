package com.easy2work.api;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * GET /api/discovery — lists public JSON endpoints (for mobile / integration).
 */
public class ApiDiscoveryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String ctx = req.getContextPath();
        List<Map<String, String>> endpoints = List.of(
                ep("GET", ctx + "/api/discovery", "This document"),
                ep("GET", ctx + "/api/health", "App + DB health"),
                ep("GET", ctx + "/api/stats", "Homepage stats (homes, hours, pros)"),
                ep("GET", ctx + "/api/services", "All service codes, titles, image URLs"),
                ep("GET", ctx + "/api/service-detail?id=CODE", "One service full detail"),
                ep("GET", ctx + "/api/bookings?phone=...", "Bookings by mobile (pending/completed/cancelled)"),
                ep("POST", ctx + "/api/booking", "Create booking (JSON body)")
        );
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ok", true);
        body.put("name", "Easy 2 Work API");
        body.put("version", "1");
        body.put("endpoints", endpoints);
        resp.setHeader("Cache-Control", "public, max-age=600");
        ApiJson.write(resp, HttpServletResponse.SC_OK, body);
    }

    private static Map<String, String> ep(String method, String path, String description) {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("method", method);
        m.put("path", path);
        m.put("description", description);
        return m;
    }
}
