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
 * Servlet for handling user registration requests.
 */
public class UserRegisterServlet extends HttpServlet {

    private UserRepository getUserRepository() {
        DataSource ds = (DataSource) getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
        return new UserRepository(ds);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String phone = req.getParameter("phone");

        if (email == null || email.trim().isEmpty()) {
            req.setAttribute("error", "Email is required");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        }

        if (password == null || password.length() < 6) {
            req.setAttribute("error", "Password must be at least 6 characters");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("error", "Passwords do not match");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        }

        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            req.setAttribute("error", "First name and last name are required");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        }

        email = email.trim().toLowerCase();

        UserRepository userRepository = getUserRepository();
        if (userRepository.emailExists(email)) {
            req.setAttribute("error", "An account with this email already exists");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        }

        String passwordHash = PasswordUtil.hashPassword(password);
        User user = new User(email, passwordHash, firstName.trim(), lastName.trim(), phone != null ? phone.trim() : null);

        Optional<User> createdUser = userRepository.create(user);

        if (createdUser.isEmpty()) {
            req.setAttribute("error", "Failed to create account. Please try again.");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        }

        User newUser = createdUser.get();

        HttpSession session = req.getSession();
        session.setAttribute("user", newUser);
        session.setAttribute("userId", newUser.getId());
        session.setAttribute("userEmail", newUser.getEmail());
        session.setAttribute("userName", newUser.getFullName());

        resp.sendRedirect(req.getContextPath() + "/?registered=true");
    }
}
