package com.easy2work.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter to protect user routes requiring authentication.
 */
public class UserAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if (!isLoggedIn) {
            String requestURI = req.getRequestURI();
            String contextPath = req.getContextPath();
            String path = requestURI.substring(contextPath.length());

            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + path);
            return;
        }

        chain.doFilter(request, response);
    }
}
