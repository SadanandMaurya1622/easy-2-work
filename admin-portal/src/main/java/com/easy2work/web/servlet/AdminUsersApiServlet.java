package com.easy2work.web.servlet;

import com.easy2work.backend.api.ApiJson;
import com.easy2work.backend.config.DatabaseConfig;
import com.easy2work.backend.repository.UserRepository;
import com.easy2work.core.model.User;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdminUsersApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DataSource ds = (DataSource) req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
        List<User> users = new UserRepository(ds).findAllOrderByCreatedDesc(500);
        List<Map<String, Object>> rows = users.stream().map(AdminUsersApiServlet::toRow).toList();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ok", true);
        body.put("dbConfigured", ds != null);
        body.put("source", ds != null ? "mysql" : "local-file");
        body.put("count", rows.size());
        body.put("users", rows);
        ApiJson.write(resp, HttpServletResponse.SC_OK, body);
    }

    private static Map<String, Object> toRow(User user) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", user.getId());
        row.put("fullName", user.getFullName());
        row.put("email", user.getEmail());
        row.put("phone", user.getPhone());
        row.put("role", user.getRole() == null ? "" : user.getRole().name());
        return row;
    }
}

