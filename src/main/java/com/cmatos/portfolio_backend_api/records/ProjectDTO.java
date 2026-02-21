package com.cmatos.portfolio_backend_api.records;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record ProjectDTO(
        @NotNull
        String name,
        @URL
        String documetationUrl,
        @URL
        String interfaceUrl,
        @URL
        String githubUrl,
        @URL
        String imageUrl,
        String observations,
        List<SkillDTO> skills
) {
}
