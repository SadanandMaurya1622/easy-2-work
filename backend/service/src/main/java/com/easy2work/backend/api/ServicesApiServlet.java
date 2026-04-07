package com.easy2work.backend.api;

import com.easy2work.backend.catalog.ManagedServiceCatalog;
import com.easy2work.catalog.ServiceCatalog;
import com.easy2work.catalog.ServiceDetail;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * GET /api/services — list all services for apps / AJAX.
 */
public class ServicesApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String ctx = req.getContextPath();
        List<Map<String, Object>> items = new ArrayList<>();
        for (ServiceDetail d : ServiceCatalog.allServices()) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("code", d.getCode());
            row.put("title", d.getTitle());
            row.put("summary", d.getSummary());
            row.put("priceLabel", d.getPriceLabel());
            row.put("priceDetail", d.getPriceDetail());
            row.put("imageUrl", d.getImageUrl());
            row.put("detailPath", ctx + "/service?id=" + d.getCode());
            items.add(row);
        }
        for (ManagedServiceCatalog.ManagedService s : ManagedServiceCatalog.list()) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("code", s.code());
            row.put("title", s.title());
            row.put("summary", s.summary());
            row.put("priceLabel", s.priceLabel());
            row.put("priceDetail", "Final price depends on scope.");
            row.put("imageUrl", s.imageDataUrl());
            row.put("detailPath", ctx + "/service?id=" + s.code());
            items.add(row);
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ok", true);
        body.put("count", items.size());
        body.put("services", items);
        resp.setHeader("Cache-Control", "public, max-age=300");
        ApiJson.write(resp, HttpServletResponse.SC_OK, body);
    }
}
