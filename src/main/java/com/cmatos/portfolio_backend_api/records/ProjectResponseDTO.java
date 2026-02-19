package com.cmatos.portfolio_backend_api.records;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponseDTO(
        Long id,
        String name,
        String documetationUrl,
        String interfaceUrl,
        String githubUrl,
        String imageUrl,
        String observations,
        LocalDateTime updatedAt,
        List<SkillDTO> skills
) {
}
