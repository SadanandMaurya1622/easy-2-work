package com.easy2work.web.servlet;

import com.easy2work.backend.config.DatabaseConfig;
import com.easy2work.backend.repository.ReviewRepository;
import com.easy2work.backend.review.ManagedReviewCatalog;
import com.easy2work.core.model.Review;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

public class AdminReviewsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataSource ds = (DataSource) req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
        List<Review> reviews = List.of();
        if (ds != null) {
            try {
                reviews = new ReviewRepository(ds).findAllOrderByCreatedDesc(500);
            } catch (Exception e) {
                req.setAttribute("reviewsError", e.getMessage());
            }
        } else {
            reviews = ManagedReviewCatalog.findAllOrderByCreatedDesc(500);
        }
        req.setAttribute("adminReviews", reviews);
        req.setAttribute("adminReviewCount", reviews.size());
        req.setAttribute("dbConfigured", true);
        req.getRequestDispatcher("/WEB-INF/jsp/admin-reviews.jsp").forward(req, resp);
    }
}
