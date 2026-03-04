package com.cmatos.portfolio_backend_api.records;

public record UserResponseDTO(
        String userName,
        String email,
        String role
) {
}
