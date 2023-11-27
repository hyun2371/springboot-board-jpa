package com.kdt.simpleboard.post.controller;

import com.kdt.simpleboard.BaseIntegrationTest;
import com.kdt.simpleboard.data.PostData;
import com.kdt.simpleboard.post.domain.Post;
import com.kdt.simpleboard.post.repository.PostRepository;
import com.kdt.simpleboard.user.UserData;
import com.kdt.simpleboard.user.domain.User;
import com.kdt.simpleboard.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.kdt.simpleboard.post.dto.PostRequest.CreatePostRequest;
import static com.kdt.simpleboard.post.dto.PostRequest.ModifyPostRequest;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

class PostControllerTest extends BaseIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("게시물 생성 api 호출에 성공한다")
    void createPost() throws Exception {
        User savedUser = userRepository.save(UserData.user());
        CreatePostRequest createPostRequest = PostData.createPostRequest(savedUser.getId());
        mvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(asJsonString(createPostRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdId").isNotEmpty());
    }

    @Test
    @DisplayName("게시물 업데이트 api 호출에 성공한다")
    void updatePost() throws Exception {
        Post savedPost = postRepository.save(PostData.post());
        userRepository.save(UserData.user());

        ModifyPostRequest modifyPostRequest = PostData.modifyPostRequest();
        mvc.perform(post("/posts/{id}", savedPost.getId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(asJsonString(modifyPostRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(modifyPostRequest.title()))
                .andExpect(jsonPath("$.content").value(modifyPostRequest.content()))
        ;
    }

    @Test
    @DisplayName("게시물 단건 조회 api 호출에 성공한다")
    void findPostById() throws Exception {
        Post post = PostData.post();
        postRepository.save(post);
        mvc.perform(get("/posts/{id}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
        ;
    }

    @Test
    @DisplayName("전체 게시물을 조회할 수 있다.")
    void findAll() throws Exception {
        User user = UserData.user();
        userRepository.save(user);

        List<Post> posts = PostData.getPosts();
        ReflectionTestUtils.setField(posts.get(0), "user", user);
        ReflectionTestUtils.setField(posts.get(1), "user", user);
        postRepository.saveAll(posts);


        mvc.perform(get("/posts")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems", is(posts.size())))
                .andExpect(jsonPath("$.items[0].title", is(posts.get(0).getTitle())))
        ;
    }
}
