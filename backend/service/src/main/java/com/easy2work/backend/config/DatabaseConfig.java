package com.easy2work.backend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Production-style pool: env vars override web.xml context-params.
 * E2W_JDBC_URL, E2W_JDBC_USER, E2W_JDBC_PASSWORD
 */
public class DatabaseConfig implements ServletContextListener {

    public static final String CTX_DATASOURCE = "com.easy2work.DataSource";

    private static final Logger LOG = Logger.getLogger(DatabaseConfig.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        String url = firstNonBlank(System.getenv("E2W_JDBC_URL"), ctx.getInitParameter("jdbcUrl"));
        String user = firstNonBlank(System.getenv("E2W_JDBC_USER"), ctx.getInitParameter("jdbcUser"));
        String password = firstNonBlank(System.getenv("E2W_JDBC_PASSWORD"), ctx.getInitParameter("jdbcPassword"));

        if (isBlank(url) || isBlank(user)) {
            LOG.warning("MySQL not configured (set E2W_JDBC_URL / E2W_JDBC_USER). Stats API will use defaults.");
            return;
        }

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(user);
        cfg.setPassword(password == null ? "" : password);
        cfg.setPoolName("Easy2WorkPool");
        cfg.setMaximumPoolSize(10);
        cfg.setMinimumIdle(2);
        cfg.setConnectionTimeout(30_000);
        cfg.setIdleTimeout(600_000);
        cfg.setMaxLifetime(1_800_000);
        cfg.addDataSourceProperty("cachePrepStmts", "true");
        cfg.addDataSourceProperty("prepStmtCacheSize", "256");
        cfg.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        cfg.addDataSourceProperty("useServerPrepStmts", "true");
        cfg.addDataSourceProperty("characterEncoding", "utf8");

        try {
            HikariDataSource ds = new HikariDataSource(cfg);
            ctx.setAttribute(CTX_DATASOURCE, ds);
            LOG.info("HikariCP pool started.");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to start datasource", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Object o = sce.getServletContext().getAttribute(CTX_DATASOURCE);
        if (o instanceof HikariDataSource ds) {
            ds.close();
        }
    }

    private static String firstNonBlank(String a, String b) {
        if (!isBlank(a)) {
            return a.trim();
        }
        if (!isBlank(b)) {
            return b.trim();
        }
        return "";
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
