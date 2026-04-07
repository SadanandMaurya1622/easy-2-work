package com.easy2work.web.servlet;

import com.easy2work.backend.config.DatabaseConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminSettingsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean dbConfigured = req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE) != null;
        req.setAttribute("dbConfigured", dbConfigured);
        req.setAttribute("serverPort", req.getServerPort());
        req.setAttribute("adminEmail", req.getSession().getAttribute("adminEmail"));
        req.getRequestDispatcher("/WEB-INF/jsp/admin-settings.jsp").forward(req, resp);
    }
}
