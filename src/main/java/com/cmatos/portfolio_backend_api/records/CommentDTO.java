package com.cmatos.portfolio_backend_api.records;

import jakarta.validation.constraints.NotNull;

public record CommentDTO(@NotNull String comment, @NotNull Long projectId, String userName) {
}
