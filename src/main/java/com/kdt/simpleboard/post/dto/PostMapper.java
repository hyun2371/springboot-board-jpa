package com.kdt.simpleboard.post.dto;

import com.kdt.simpleboard.post.domain.Post;
import com.kdt.simpleboard.post.dto.request.CreatePostRequest;
import com.kdt.simpleboard.post.dto.response.CreatePostResponse;
import com.kdt.simpleboard.post.dto.response.FindPostResponse;
import com.kdt.simpleboard.user.domain.User;

public class PostMapper {
    public static CreatePostResponse toCreatePostRes(Post post) {
        return new CreatePostResponse(post.getId());
    }

    public static Post toPostEntity(CreatePostRequest request, User user) {
        return Post.builder()
                .title(request.title())
                .content(request.content())
                .user(user)
                .build();
    }

    public static FindPostResponse toFindPostRes(Post post) {
        return FindPostResponse.builder()
                .userId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }
}
