package com.easy2work.web.servlet;

import com.easy2work.backend.config.DatabaseConfig;
import com.easy2work.backend.repository.UserRepository;
import com.easy2work.core.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

public class AdminUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataSource ds = (DataSource) req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
        List<User> users = new UserRepository(ds).findAllOrderByCreatedDesc(500);
        req.setAttribute("adminUsers", users);
        req.setAttribute("adminUserCount", users.size());
        req.setAttribute("dbConfigured", ds != null);
        req.getRequestDispatcher("/WEB-INF/jsp/admin-users.jsp").forward(req, resp);
    }
}
