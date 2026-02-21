package com.cmatos.portfolio_backend_api.records;

import java.util.List;

public record ValidationErrorResponse (
        String error,
        List<FieldDetailedError> fieldErrors
) {
    public record FieldDetailedError(String field, String message) {}
}
