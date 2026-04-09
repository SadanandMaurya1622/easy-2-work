package com.easy2work.web.servlet;

import com.easy2work.backend.catalog.ManagedServiceCatalog;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class AdminServicesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("services", ManagedServiceCatalog.list());
        String servletPath = req.getServletPath();
        String view = "/services-list".equals(servletPath)
                ? "/WEB-INF/jsp/admin-services-list.jsp"
                : "/WEB-INF/jsp/admin-services.jsp";
        req.getRequestDispatcher(view).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = n(req.getParameter("action"));
        if ("add".equals(action)) {
            String code = n(req.getParameter("code")).toUpperCase();
            String title = n(req.getParameter("title"));
            String priceLabel = n(req.getParameter("basePrice"));
            String summary = n(req.getParameter("description"));
            String imageDataUrl = readImageDataUrl(req.getPart("image"));
            String priceDetail = n(req.getParameter("priceDetail"));
            List<String> weProvide = lines(req.getParameter("weProvide"));
            List<String> fromYou = lines(req.getParameter("fromYou"));
            List<String> notIncluded = lines(req.getParameter("notIncluded"));
            List<String> visitSteps = lines(req.getParameter("visitSteps"));
            if (!title.isBlank()) {
                ManagedServiceCatalog.add(
                        code,
                        title,
                        summary,
                        priceLabel.isBlank() ? "On request" : priceLabel,
                        imageDataUrl,
                        priceDetail,
                        weProvide,
                        fromYou,
                        notIncluded,
                        visitSteps
                );
            }
        } else if ("delete".equals(action)) {
            String id = n(req.getParameter("id"));
            ManagedServiceCatalog.deleteById(id);
        }
        String redirectPath = "delete".equals(action) ? "/services-list" : "/services";
        resp.sendRedirect(req.getContextPath() + redirectPath);
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
                .map(AdminServicesServlet::n)
                .filter(s -> !s.isBlank())
                .toList();
    }
}
