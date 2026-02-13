package com.cmatos.portfolio_backend_api.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDTO(
        @NotBlank(message = "O nome é obrigatório")
        String username,
        @NotBlank(message = "A senha é obrigatória")
        String password,
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email
) {
}
