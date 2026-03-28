package com.easy2work.api;

import com.easy2work.catalog.ServiceCatalog;
import com.easy2work.catalog.ServiceDetail;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * GET /api/service-detail?id=CODE — full detail JSON (same content as web page).
 */
public class ServiceDetailApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String raw = req.getParameter("id");
        if (raw == null) {
            raw = "";
        }
        var opt = ServiceCatalog.find(raw);
        if (opt.isEmpty()) {
            ApiJson.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "Unknown service id");
            return;
        }
        ServiceDetail d = opt.get();
        String ctx = req.getContextPath();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ok", true);
        body.put("code", d.getCode());
        body.put("title", d.getTitle());
        body.put("summary", d.getSummary());
        body.put("imageUrl", d.getImageUrl());
        body.put("weProvide", d.getWeProvide());
        body.put("fromYou", d.getFromYou());
        body.put("notIncluded", d.getNotIncluded());
        body.put("visitSteps", d.getVisitSteps());
        body.put("bookPath", ctx + "/book.jsp?serviceType=" + d.getCode());
        resp.setHeader("Cache-Control", "public, max-age=300");
        ApiJson.write(resp, HttpServletResponse.SC_OK, body);
    }
}
