package com.easy2work.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminServicesServlet extends HttpServlet {

    private static final String CTX_KEY = "adminServices";

    public static final class ServiceRow {
        private final String id;
        private final String code;
        private final String title;
        private final String basePrice;
        private final String description;

        public ServiceRow(String id, String code, String title, String basePrice, String description) {
            this.id = id;
            this.code = code;
            this.title = title;
            this.basePrice = basePrice;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getTitle() {
            return title;
        }

        public String getBasePrice() {
            return basePrice;
        }

        public String getDescription() {
            return description;
        }
    }

    @Override
    public void init() throws ServletException {
        if (getServletContext().getAttribute(CTX_KEY) == null) {
            List<ServiceRow> initial = new ArrayList<>();
            initial.add(new ServiceRow(UUID.randomUUID().toString(), "ELECTRICAL", "Electrical Repair", "From 199", "Switches, wiring and small electrical fixes"));
            initial.add(new ServiceRow(UUID.randomUUID().toString(), "AC", "AC Servicing", "From 449", "Split AC cleaning and basic performance checks"));
            initial.add(new ServiceRow(UUID.randomUUID().toString(), "BATHROOM", "Bathroom Cleaning", "From 399", "Deep cleaning and sanitization"));
            getServletContext().setAttribute(CTX_KEY, initial);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("services", services());
        req.getRequestDispatcher("/WEB-INF/jsp/admin-services.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = n(req.getParameter("action"));
        if ("add".equals(action)) {
            String code = n(req.getParameter("code")).toUpperCase();
            String title = n(req.getParameter("title"));
            String basePrice = n(req.getParameter("basePrice"));
            String description = n(req.getParameter("description"));
            if (!code.isBlank() && !title.isBlank()) {
                services().add(new ServiceRow(
                        UUID.randomUUID().toString(),
                        code,
                        title,
                        basePrice.isBlank() ? "N/A" : basePrice,
                        description
                ));
            }
        } else if ("delete".equals(action)) {
            String id = n(req.getParameter("id"));
            services().removeIf(s -> s.getId().equals(id));
        }
        resp.sendRedirect(req.getContextPath() + "/services");
    }

    @SuppressWarnings("unchecked")
    private List<ServiceRow> services() {
        return (List<ServiceRow>) getServletContext().getAttribute(CTX_KEY);
    }

    private static String n(String v) {
        return v == null ? "" : v.trim();
    }
}
