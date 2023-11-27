package com.kdt.simpleboard.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@Getter
public class PostRequest {
    public record CreatePostRequest(
            Long userId,

            @NotBlank(message = "제목을 입력해주세요")
            String title,

            @NotBlank(message = "내용을 입력해주세요")
            String content
    ) {
    }

    public record ModifyPostRequest(
            @NotBlank(message = "제목을 입력해주세요")
            String title,

            @NotBlank(message = "내용을 입력해주세요")
            String content
    ) {
    }
}
