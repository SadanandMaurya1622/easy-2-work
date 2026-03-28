package com.easy2work.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class StatsRepository {

    private final DataSource dataSource;

    public StatsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<SiteStats> findSingleton() throws SQLException {
        final String sql = "SELECT homes_serviced, hours_saved, verified_professionals FROM site_stats WHERE id = 1";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional.of(new SiteStats(
                    rs.getInt("homes_serviced"),
                    rs.getInt("hours_saved"),
                    rs.getInt("verified_professionals")
            ));
        }
    }

    public boolean ping() {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT 1");
             ResultSet rs = ps.executeQuery()) {
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public record SiteStats(int homesServiced, int hoursSaved, int verifiedProfessionals) {}
}
