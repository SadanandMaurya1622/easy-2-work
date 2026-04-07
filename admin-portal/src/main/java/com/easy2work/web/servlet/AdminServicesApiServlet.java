package com.easy2work.web.servlet;

import com.easy2work.backend.api.ApiJson;
import com.easy2work.backend.catalog.ManagedServiceCatalog;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class AdminServicesApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ok", true);
        body.put("count", ManagedServiceCatalog.list().size());
        body.put("services", ManagedServiceCatalog.list());
        ApiJson.write(resp, HttpServletResponse.SC_OK, body);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String code = n(req.getParameter("code"));
            String title = n(req.getParameter("title"));
            String summary = n(req.getParameter("summary"));
            String priceLabel = n(req.getParameter("priceLabel"));
            String imageDataUrl = readImageDataUrl(req.getPart("image"));
            String priceDetail = n(req.getParameter("priceDetail"));
            List<String> weProvide = lines(req.getParameter("weProvide"));
            List<String> fromYou = lines(req.getParameter("fromYou"));
            List<String> notIncluded = lines(req.getParameter("notIncluded"));
            List<String> visitSteps = lines(req.getParameter("visitSteps"));
            if (title.isBlank()) {
                ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "title is required");
                return;
            }
            var row = ManagedServiceCatalog.add(
                    code,
                    title,
                    summary,
                    priceLabel,
                    imageDataUrl,
                    priceDetail,
                    weProvide,
                    fromYou,
                    notIncluded,
                    visitSteps
            );
            ApiJson.write(resp, HttpServletResponse.SC_CREATED, Map.of("ok", true, "service", row));
        } catch (Exception e) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Unable to add service");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = n(req.getParameter("id"));
        if (id.isBlank()) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "id is required");
            return;
        }
        ManagedServiceCatalog.deleteById(id);
        ApiJson.write(resp, HttpServletResponse.SC_OK, Map.of("ok", true));
    }

    private static String readImageDataUrl(Part imagePart) throws IOException {
        if (imagePart == null || imagePart.getSize() <= 0) {
            return "";
        }
        String contentType = imagePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return "";
        }
        byte[] bytes = imagePart.getInputStream().readAllBytes();
        String encoded = java.util.Base64.getEncoder().encodeToString(bytes);
        return "data:" + contentType + ";base64," + encoded;
    }

    private static String n(String v) {
        return v == null ? "" : v.trim();
    }

    private static List<String> lines(String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(raw.split("\\R"))
                .map(AdminServicesApiServlet::n)
                .filter(s -> !s.isBlank())
                .toList();
    }
}
