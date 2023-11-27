package com.kdt.simpleboard.post.service;

import com.kdt.simpleboard.common.dto.PageResponse;
import com.kdt.simpleboard.common.exception.CustomException;
import com.kdt.simpleboard.common.exception.ErrorCode;
import com.kdt.simpleboard.data.PostData;
import com.kdt.simpleboard.post.domain.Post;
import com.kdt.simpleboard.post.repository.PostRepository;
import com.kdt.simpleboard.user.UserData;
import com.kdt.simpleboard.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.kdt.simpleboard.post.dto.PostRequest.CreatePostRequest;
import static com.kdt.simpleboard.post.dto.PostRequest.ModifyPostRequest;
import static com.kdt.simpleboard.post.dto.PostResponse.CreatePostResponse;
import static com.kdt.simpleboard.post.dto.PostResponse.FindPostResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Mock
    private UserService userService;


    @Test
    @DisplayName("게시판 글 생성에 성공한다.")
    void createBoardSuccess() {
        CreatePostRequest createPostRequest = PostData.createPostRequest();
        Post post = PostData.post();

        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(userService.getUserEntity(any(Long.class))).thenReturn(UserData.user());

        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);

        assertEquals(post.getId(), createPostResponse.createdId());
    }

    @Test
    @DisplayName("게시물 수정에 성공한다.")
    void update() {
        Post post = PostData.post();
        setField(post, "id", 1L);
        ModifyPostRequest modifyPostRequest = PostData.modifyPostRequest();
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));

        FindPostResponse findPostResponse = postService.updatePost(post.getId(), modifyPostRequest);

        assertAll(
                () -> assertThat(findPostResponse.title()).isEqualTo(modifyPostRequest.title()),
                () -> assertThat(findPostResponse.content()).isEqualTo(modifyPostRequest.content())
        );
    }

    @Test
    @DisplayName("게시물 수정에 실패한다.")
    void updateFail() {
        Post post = PostData.post();
        setField(post, "id", 1L);

        ModifyPostRequest modifyPostRequest = PostData.modifyPostRequest();
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, ()
                -> postService.updatePost(post.getId(), modifyPostRequest));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_EXIST_POST_ID);
    }

    @Test
    @DisplayName("id로 게시물을 조회할 수 있다.")
    void findById() {
        Post post = PostData.post();
        setField(post, "id", 1L);
        setField(post.getUser(), "id", 1L);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        FindPostResponse boardRes = postService.findById(1L);
        assertAll(
                () -> assertThat(boardRes.content()).isEqualTo(post.getContent()),
                () -> assertThat(boardRes.title()).isEqualTo(post.getTitle()),
                () -> assertThat(boardRes.userId()).isEqualTo(post.getUser().getId())
        );
    }


    @Test
    @DisplayName("모든 게시물을 조회할 수 있다")
    void findAll() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> posts = PostData.getPosts();
        Page<Post> pagedPosts = new PageImpl<>(posts, pageable, posts.size());

        when(postRepository.findAll(pageable)).thenReturn(pagedPosts);

        PageResponse<FindPostResponse> response = postService.findAll(pageable);
        assertEquals(posts.size(), response.getItems().size());
        assertEquals(pagedPosts.getTotalElements(), response.getTotalItems());

        assertEquals(posts.get(0).getTitle(), response.getItems().get(0).title());
        assertEquals(posts.get(0).getContent(), response.getItems().get(0).content());

        assertAll(
                () -> assertEquals(posts.get(0).getTitle(), response.getItems().get(0).title()),
                () -> assertEquals(posts.get(0).getContent(), response.getItems().get(0).content())
        );
    }
}