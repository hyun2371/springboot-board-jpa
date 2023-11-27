package com.kdt.simpleboard.post.controller;

import com.kdt.simpleboard.BaseIntegrationTest;
import com.kdt.simpleboard.common.dto.PageResponse;
import com.kdt.simpleboard.data.PostData;
import com.kdt.simpleboard.post.domain.Post;
import com.kdt.simpleboard.post.dto.PostRequest;
import com.kdt.simpleboard.post.repository.PostRepository;
import com.kdt.simpleboard.post.service.PostService;
import com.kdt.simpleboard.user.UserData;
import com.kdt.simpleboard.user.domain.User;
import com.kdt.simpleboard.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.kdt.simpleboard.post.dto.PostRequest.ModifyPostRequest;
import static com.kdt.simpleboard.post.dto.PostResponse.FindPostResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends BaseIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    private User user;
   private Post post;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserData.user());
        post = postRepository.save(PostData.post(user));
    }

    @Test
    @DisplayName("게시물 생성 api 호출에 성공한다")
    void createPost() throws Exception {
        PostRequest.CreatePostRequest createPostRequest = PostData.createPostRequest(user.getId());
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

        ModifyPostRequest modifyPostRequest = PostData.modifyPostRequest();
        mvc.perform(post("/posts/{id}", post.getId())
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
        FindPostResponse findPostResponse = postService.findById(post.getId());
        mvc.perform(get("/posts/{id}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(findPostResponse.title()))
                .andExpect(jsonPath("$.content").value(findPostResponse.content()))
        ;
    }

    @Test
    @DisplayName("전체 게시물을 조회할 수 있다.")
    void findAll() throws Exception {
        Post post2 = postRepository.save(PostData.post(user));

        List<Post> posts = List.of(post, post2);
        PageResponse<FindPostResponse> pagedFindPostResponse = postService.findAll(PageRequest.of(0, 10));
        mvc.perform(get("/posts")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems", is(posts.size())))
                .andExpect(jsonPath("$.items[0].title", is(pagedFindPostResponse.getItems().get(0).title())))
        ;
    }
}
