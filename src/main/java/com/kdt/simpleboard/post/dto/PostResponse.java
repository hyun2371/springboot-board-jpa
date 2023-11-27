package com.kdt.simpleboard.post.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
@Builder
public class PostResponse {

    public record CreatePostResponse(
            Long createdId
    ) {
    }

    @Builder
    public record FindPostResponse(
            Long userId,
            String title,
            String content
    ) {
    }
}
