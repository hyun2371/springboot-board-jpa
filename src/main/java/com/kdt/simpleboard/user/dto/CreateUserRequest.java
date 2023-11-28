package com.kdt.simpleboard.user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateUserRequest(
        @NotBlank(message = "이름을 입력해주세요.")
        String name,

        @Min(0)
        int age,

        @NotBlank(message = "취미를 입력해주세요.")
        String hobby
) {
}
