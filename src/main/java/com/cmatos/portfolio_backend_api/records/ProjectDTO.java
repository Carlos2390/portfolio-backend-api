package com.cmatos.portfolio_backend_api.records;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record ProjectDTO(
        @NotBlank(message = "O nome do projeto é obrigatório")
        String name,
        @URL(message = "A documentação deve ser uma URL válida")
        String documentationUrl,
        @URL(message = "A interface deve ser uma URL válida")
        String interfaceUrl,
        @URL(message = "O repositório deve ser uma URL válida")
        String githubUrl,
        @URL(message = "A imagem deve ser uma URL válida")
        String imageUrl,
        String observations,
        List<SkillDTO> skills
) {
}
