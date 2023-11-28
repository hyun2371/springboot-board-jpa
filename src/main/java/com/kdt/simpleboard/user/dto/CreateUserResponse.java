package com.kdt.simpleboard.user.dto;

import lombok.Builder;

@Builder
public record CreateUserResponse(
        Long createdId
) {
}
