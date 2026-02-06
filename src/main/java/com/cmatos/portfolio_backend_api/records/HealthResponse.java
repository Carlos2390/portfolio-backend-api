package com.cmatos.portfolio_backend_api.records;

import java.util.Map;

public record HealthResponse(String updated_at, Map<String, DatabaseStatus> dependencies) {
}
