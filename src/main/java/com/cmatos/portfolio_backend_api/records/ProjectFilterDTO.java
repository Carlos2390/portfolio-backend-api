package com.cmatos.portfolio_backend_api.records;

import java.util.List;

public record ProjectFilterDTO(
        String name,
        List<String> skills
) {
}
