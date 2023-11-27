package com.kdt.simpleboard.data;

import com.kdt.simpleboard.post.domain.Post;
import com.kdt.simpleboard.user.UserData;
import com.kdt.simpleboard.user.domain.User;

import java.util.List;

import static com.kdt.simpleboard.post.dto.PostRequest.CreatePostRequest;
import static com.kdt.simpleboard.post.dto.PostRequest.ModifyPostRequest;
import static com.kdt.simpleboard.post.dto.PostResponse.CreatePostResponse;
import static com.kdt.simpleboard.post.dto.PostResponse.FindPostResponse;

public class PostData {

    public static CreatePostRequest createPostRequest() {
        return new CreatePostRequest(1L, "titleA", "contentA");
    }

    public static CreatePostRequest createPostRequest(Long userId) {
        return new CreatePostRequest(userId, "titleA", "contentA");
    }

    public static ModifyPostRequest modifyPostRequest() {
        return new ModifyPostRequest("titleAChanged", "contentAChanged");
    }

    public static CreatePostResponse createPostResponse() {
        return new CreatePostResponse(1L);
    }

    public static FindPostResponse findPostResponse() {
        return new FindPostResponse(1L, "titleA", "contentA");
    }

    public static Post post() {
        return Post.builder()
                .title("titleA")
                .content("contentA")
                .user(UserData.user())
                .build();
    }

    public static Post post(User user) {
        return Post.builder()
                .title("titleA")
                .content("contentA")
                .user(user)
                .build();
    }


    public static Post post(String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .user(UserData.user())
                .build();
    }

    public static List<Post> getPosts() {
        Post board1 = post();
        Post board2 = post("titleB", "contentB");

        return List.of(board1, board2);
    }
}
