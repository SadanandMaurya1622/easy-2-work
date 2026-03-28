package com.easy2work.api;

import com.easy2work.config.DataSourceListener;
import com.easy2work.db.StatsRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class HealthApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "UP");

        DataSource ds = (DataSource) req.getServletContext().getAttribute(DataSourceListener.CTX_DATASOURCE);
        if (ds == null) {
            body.put("database", "NOT_CONFIGURED");
            ApiJson.write(resp, HttpServletResponse.SC_OK, body);
            return;
        }

        boolean ok = new StatsRepository(ds).ping();
        body.put("database", ok ? "OK" : "DOWN");
        int status = ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_SERVICE_UNAVAILABLE;
        ApiJson.write(resp, status, body);
    }
}
