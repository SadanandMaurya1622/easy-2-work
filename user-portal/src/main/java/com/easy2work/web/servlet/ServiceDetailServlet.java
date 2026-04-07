package com.easy2work.web.servlet;

import com.easy2work.backend.catalog.ManagedServiceCatalog;
import com.easy2work.catalog.ServiceCatalog;
import com.easy2work.catalog.ServiceDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Loads {@link ServiceDetail} and forwards to the JSP so the view does not import the catalog (fewer JSP compile issues).
 */
public class ServiceDetailServlet extends HttpServlet {

    /** Request attribute key for {@link ServiceDetail} on the forwarded JSP. */
    public static final String ATTR_SERVICE = "svcDetail";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rawId = req.getParameter("id");
        if (rawId == null) {
            rawId = "";
        }
        var opt = ServiceCatalog.find(rawId);
        ServiceDetail detail;
        if (opt.isPresent()) {
            detail = opt.get();
        } else {
            var managed = ManagedServiceCatalog.findByCode(rawId);
            if (managed.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/#services");
                return;
            }
            detail = ManagedServiceCatalog.toServiceDetail(managed.get());
        }
        req.setAttribute(ATTR_SERVICE, detail);
        req.getRequestDispatcher("/service-detail.jsp").forward(req, resp);
    }
}
