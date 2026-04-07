package com.easy2work.web.servlet;

import com.easy2work.backend.config.DatabaseConfig;
import com.easy2work.backend.repository.UserRepository;
import com.easy2work.core.model.User;
import com.easy2work.core.security.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Optional;

/**
 * Servlet for handling admin login requests.
 */
public class AdminLoginServlet extends HttpServlet {
    private static final String DEMO_ADMIN_EMAIL = "admin@easy2work.com";
    private static final String DEMO_ADMIN_PASSWORD = "admin123";

    private UserRepository getUserRepository() {
        DataSource ds = (DataSource) getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
        return new UserRepository(ds);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/admin-login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase();

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Email and password are required");
            req.getRequestDispatcher("/WEB-INF/jsp/admin-login.jsp").forward(req, resp);
            return;
        }

        DataSource ds = (DataSource) getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
        // Dev fallback: allow login when database is not configured.
        if (ds == null && DEMO_ADMIN_EMAIL.equals(normalizedEmail) && DEMO_ADMIN_PASSWORD.equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("adminUser", DEMO_ADMIN_EMAIL);
            session.setAttribute("adminId", 0L);
            session.setAttribute("adminEmail", DEMO_ADMIN_EMAIL);
            session.setAttribute("adminName", "Demo Admin");
            resp.sendRedirect(req.getContextPath() + "/bookings");
            return;
        }

        UserRepository userRepository = getUserRepository();
        Optional<User> userOpt = userRepository.findByEmail(normalizedEmail);

        if (userOpt.isEmpty()) {
            req.setAttribute("error", "Invalid email or password");
            req.getRequestDispatcher("/WEB-INF/jsp/admin-login.jsp").forward(req, resp);
            return;
        }

        User user = userOpt.get();

        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            req.setAttribute("error", "Invalid email or password");
            req.getRequestDispatcher("/WEB-INF/jsp/admin-login.jsp").forward(req, resp);
            return;
        }

        if (!user.isAdmin()) {
            req.setAttribute("error", "Access denied. Admin privileges required.");
            req.getRequestDispatcher("/WEB-INF/jsp/admin-login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("adminUser", user);
        session.setAttribute("adminId", user.getId());
        session.setAttribute("adminEmail", user.getEmail());
        session.setAttribute("adminName", user.getFullName());

        userRepository.updateLastLogin(user.getId());

        resp.sendRedirect(req.getContextPath() + "/bookings");
    }
}
