package com.cmatos.portfolio_backend_api.services;

import com.cmatos.portfolio_backend_api.records.DatabaseStatus;
import com.cmatos.portfolio_backend_api.records.HealthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HealthService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public HealthResponse getHealth() {
        String version = jdbcTemplate.queryForObject("SELECT version();", String.class);

        Integer max_connections = jdbcTemplate.queryForObject(
                "SELECT setting::int FROM pg_settings WHERE name = 'max_connections';", Integer.class);

        Integer activeConnections = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM pg_stat_activity", Integer.class);

        DatabaseStatus databaseStatus = new DatabaseStatus(version, max_connections != null ? max_connections : 0, activeConnections != null ? activeConnections : 0);

        return new HealthResponse(java.time.LocalDateTime.now().toString(), Map.of("database", databaseStatus));
    }
}
