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
 * Servlet for handling user login requests.
 */
public class UserLoginServlet extends HttpServlet {

    private UserRepository getUserRepository() {
        DataSource ds = (DataSource) getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
        return new UserRepository(ds);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String redirect = req.getParameter("redirect");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Email and password are required");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            return;
        }

        UserRepository userRepository = getUserRepository();
        Optional<User> userOpt = userRepository.findByEmail(email.trim().toLowerCase());

        if (userOpt.isEmpty()) {
            req.setAttribute("error", "Invalid email or password");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            return;
        }

        User user = userOpt.get();

        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            req.setAttribute("error", "Invalid email or password");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            return;
        }

        if (user.isAdmin()) {
            req.setAttribute("error", "Admin users must use the admin login");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userName", user.getFullName());

        userRepository.updateLastLogin(user.getId());

        String redirectUrl = (redirect != null && !redirect.isEmpty()) ? redirect : req.getContextPath() + "/";
        resp.sendRedirect(redirectUrl);
    }
}
