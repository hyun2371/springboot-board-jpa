package com.kdt.simpleboard.post.dto.response;

import lombok.Builder;

@Builder
public record FindPostResponse(
        Long userId,
        String title,
        String content
) {
}
