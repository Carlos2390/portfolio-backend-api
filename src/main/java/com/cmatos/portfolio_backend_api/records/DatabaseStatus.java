package com.cmatos.portfolio_backend_api.records;

public record DatabaseStatus(String version, int max_connections, int opened_connections) {
}
