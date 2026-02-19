package com.cmatos.portfolio_backend_api.records;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProjectDTO(
        @NotNull
        String name,
        String documetationUrl,
        String interfaceUrl,
        String githubUrl,
        String imageUrl,
        String observations,
        List<SkillDTO> skills
) {
}
