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

public class StatsApiServlet extends HttpServlet {

    private static final int DEFAULT_HOMES = 1200;
    private static final int DEFAULT_HOURS = 850;
    private static final int DEFAULT_PROS = 50;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Cache-Control", "public, max-age=60");

        Map<String, Object> body = new LinkedHashMap<>();
        DataSource ds = (DataSource) req.getServletContext().getAttribute(DataSourceListener.CTX_DATASOURCE);
        if (ds == null) {
            body.put("source", "default");
            body.put("homesServiced", DEFAULT_HOMES);
            body.put("hoursSaved", DEFAULT_HOURS);
            body.put("verifiedProfessionals", DEFAULT_PROS);
            ApiJson.write(resp, HttpServletResponse.SC_OK, body);
            return;
        }

        try {
            var repo = new StatsRepository(ds);
            var opt = repo.findSingleton();
            if (opt.isEmpty()) {
                body.put("source", "default");
                body.put("homesServiced", DEFAULT_HOMES);
                body.put("hoursSaved", DEFAULT_HOURS);
                body.put("verifiedProfessionals", DEFAULT_PROS);
            } else {
                var s = opt.get();
                body.put("source", "mysql");
                body.put("homesServiced", s.homesServiced());
                body.put("hoursSaved", s.hoursSaved());
                body.put("verifiedProfessionals", s.verifiedProfessionals());
            }
        } catch (Exception e) {
            body.put("source", "default");
            body.put("error", "db_unavailable");
            body.put("homesServiced", DEFAULT_HOMES);
            body.put("hoursSaved", DEFAULT_HOURS);
            body.put("verifiedProfessionals", DEFAULT_PROS);
        }
        ApiJson.write(resp, HttpServletResponse.SC_OK, body);
    }
}
