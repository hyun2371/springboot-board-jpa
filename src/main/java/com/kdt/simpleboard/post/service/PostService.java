package com.kdt.simpleboard.post.service;

import com.kdt.simpleboard.common.dto.PageResponse;
import com.kdt.simpleboard.common.exception.CustomException;
import com.kdt.simpleboard.post.domain.Post;
import com.kdt.simpleboard.post.dto.PostMapper;
import com.kdt.simpleboard.post.repository.PostRepository;
import com.kdt.simpleboard.user.domain.User;
import com.kdt.simpleboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kdt.simpleboard.common.exception.ErrorCode.NOT_EXIST_POST_ID;
import static com.kdt.simpleboard.post.dto.PostMapper.*;
import static com.kdt.simpleboard.post.dto.PostRequest.CreatePostRequest;
import static com.kdt.simpleboard.post.dto.PostRequest.ModifyPostRequest;
import static com.kdt.simpleboard.post.dto.PostResponse.CreatePostResponse;
import static com.kdt.simpleboard.post.dto.PostResponse.FindPostResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request) {
        User user = userService.getUserEntity(request.userId());
        Post post = toPostEntity(request, user);
        Post savedPost = postRepository.save(post);
        return toCreatePostRes(savedPost);
    }

    @Transactional
    public FindPostResponse updatePost(Long postId, ModifyPostRequest request) {
        Post post = getPostEntity(postId);
        Post updatedPost = post.updatePostInfo(request.title(), request.content());
        return toFindPostRes(updatedPost);
    }


    public FindPostResponse findById(Long postId) {
        Post post = getPostEntity(postId);
        return toFindPostRes(post);
    }

    public PageResponse<FindPostResponse> findAll(Pageable pageable) {
        Page<Post> pagedPosts = postRepository.findAll(pageable);
        Page<FindPostResponse> pagedFindPostRes = pagedPosts.map(PostMapper::toFindPostRes);
        return PageResponse.fromPage(pagedFindPostRes);
    }

    private Post getPostEntity(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new CustomException(NOT_EXIST_POST_ID));
    }
}
