package com.easy2work.web.servlet;

import com.easy2work.backend.config.DatabaseConfig;
import com.easy2work.backend.repository.BookingRepository;
import com.easy2work.core.booking.MemoryBookingStore;
import com.easy2work.core.model.Booking;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Admin-only list of bookings.
 * <p>
 * Secret resolution (first match wins): {@code E2W_ADMIN_KEY} env → context-param {@link #CTX_PARAM_ADMIN_KEY}
 * → if JDBC is <strong>not</strong> configured, built-in demo key {@link #DEV_LOCAL_ADMIN_KEY} (local two-browser testing).
 * <p>
 * Open: {@code GET /admin/bookings?key=...} or header {@code X-Admin-Key}.
 */
public class AdminServlet extends HttpServlet {

    public static final String ENV_ADMIN_KEY = "E2W_ADMIN_KEY";
    /** Optional {@code web.xml} {@code context-param} for staging / team demo. */
    public static final String CTX_PARAM_ADMIN_KEY = "easy2workAdminKey";
    /**
     * Fixed key only when MySQL is off — same JVM memory as user bookings so two browsers on localhost work.
     * <strong>Not used when datasource is configured</strong> (production must set env or context-param).
     */
    public static final String DEV_LOCAL_ADMIN_KEY = "easy2work-local-dev";

    private static final Logger LOG = Logger.getLogger(AdminServlet.class.getName());
    private static volatile boolean loggedDevKey;

    private static final int DB_LIMIT = 500;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        boolean sessionAuthenticated = session != null && session.getAttribute("adminEmail") != null;
        ServletContext ctx = req.getServletContext();
        String expected = resolveExpectedSecret(ctx);
        if (expected.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
            resp.setContentType("text/plain;charset=UTF-8");
            PrintWriter w = resp.getWriter();
            w.println("Easy 2 Work — admin bookings disabled");
            w.println();
            w.println("MySQL is connected (E2W_JDBC_URL / pool active) but no admin secret is set.");
            w.println("Do one of the following, then restart the server:");
            w.println("  • Set environment variable E2W_ADMIN_KEY to a long random secret");
            w.println("  • Or set context-param easy2workAdminKey in WEB-INF/web.xml");
            w.println();
            w.println("Then open in the browser:");
            w.println("  /admin/bookings?key=<your-secret>");
            w.println("  (alias: /admin-bookings?key=<your-secret>)");
            w.println();
            w.println("Local demo without database: unset E2W_JDBC_URL and E2W_JDBC_USER, restart, then:");
            w.println("  /admin/bookings?key=easy2work-local-dev");
            return;
        }
        if (expected.equals(DEV_LOCAL_ADMIN_KEY) && !loggedDevKey) {
            loggedDevKey = true;
            LOG.log(Level.WARNING, "Admin bookings: using built-in demo key {0} (no JDBC). Set " + ENV_ADMIN_KEY + " for production.",
                    DEV_LOCAL_ADMIN_KEY);
        }

        if (!sessionAuthenticated) {
            String provided = req.getHeader("X-Admin-Key");
            if (provided == null || provided.isBlank()) {
                provided = req.getParameter("key");
            }
            if (provided == null || !expected.equals(provided)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        try {
            List<Booking> rows = load(req);
            req.setAttribute("adminBookings", rows);
            req.setAttribute("adminBookingCount", rows.size());
            req.setAttribute("adminSource", dataSourceConfigured(req) ? "MySQL" : "In-memory (demo — lost on restart)");
            if (DEV_LOCAL_ADMIN_KEY.equals(expected)) {
                req.setAttribute("adminDemoKeyHint", DEV_LOCAL_ADMIN_KEY);
            }
            req.getRequestDispatcher("/bookings.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    static String resolveExpectedSecret(ServletContext ctx) {
        String env = System.getenv(ENV_ADMIN_KEY);
        if (env != null && !env.isBlank()) {
            return env.trim();
        }
        String param = ctx.getInitParameter(CTX_PARAM_ADMIN_KEY);
        if (param != null && !param.isBlank()) {
            return param.trim();
        }
        if (ctx.getAttribute(DatabaseConfig.CTX_DATASOURCE) == null) {
            return DEV_LOCAL_ADMIN_KEY;
        }
        return "";
    }

    private static boolean dataSourceConfigured(HttpServletRequest req) {
        return req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE) != null;
    }

    private static List<Booking> load(HttpServletRequest req) throws Exception {
        DataSource ds = (DataSource) req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
        if (ds != null) {
            return new BookingRepository(ds).findAllOrderByCreatedDesc(DB_LIMIT);
        }
        MemoryBookingStore mem = (MemoryBookingStore) req.getServletContext().getAttribute(MemoryBookingStore.SERVLET_CONTEXT_KEY);
        if (mem == null) {
            return List.of();
        }
        return mem.listAllByCreatedDesc();
    }
}
