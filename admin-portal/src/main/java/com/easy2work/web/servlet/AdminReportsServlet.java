package com.easy2work.web.servlet;

import com.easy2work.backend.config.DatabaseConfig;
import com.easy2work.backend.repository.BookingRepository;
import com.easy2work.backend.repository.ReviewRepository;
import com.easy2work.backend.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;

public class AdminReportsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataSource ds = (DataSource) req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
        int bookingCount = 0;
        int userCount = 0;
        int reviewCount = 0;
        if (ds != null) {
            try {
                bookingCount = new BookingRepository(ds).findAllOrderByCreatedDesc(1000).size();
                userCount = new UserRepository(ds).findAllOrderByCreatedDesc(1000).size();
                reviewCount = new ReviewRepository(ds).findAllOrderByCreatedDesc(1000).size();
            } catch (Exception e) {
                req.setAttribute("reportsError", e.getMessage());
            }
        }
        req.setAttribute("bookingCount", bookingCount);
        req.setAttribute("userCount", userCount);
        req.setAttribute("reviewCount", reviewCount);
        req.setAttribute("dbConfigured", ds != null);
        req.getRequestDispatcher("/WEB-INF/jsp/admin-reports.jsp").forward(req, resp);
    }
}
