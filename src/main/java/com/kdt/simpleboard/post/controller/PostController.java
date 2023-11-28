package com.kdt.simpleboard.post.controller;

import com.kdt.simpleboard.common.dto.PageResponse;
import com.kdt.simpleboard.post.dto.request.CreatePostRequest;
import com.kdt.simpleboard.post.dto.response.CreatePostResponse;
import com.kdt.simpleboard.post.dto.response.FindPostResponse;
import com.kdt.simpleboard.post.dto.request.ModifyPostRequest;
import com.kdt.simpleboard.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시물 생성")
    @PostMapping
    public ResponseEntity<CreatePostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        CreatePostResponse response = postService.createPost(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시물 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 생성 성공",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/{id}")
    public ResponseEntity<FindPostResponse> updatePost(@PathVariable("id") Long postId, @Valid @RequestBody ModifyPostRequest request) {
        FindPostResponse response = postService.updatePost(postId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시물 단건 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 생성 성공",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<FindPostResponse> findPost(@PathVariable("id") Long postId) {
        FindPostResponse response = postService.findById(postId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시물 모두 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 생성 성공",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<PageResponse<FindPostResponse>> findAll(Pageable pageable) {
        PageResponse<FindPostResponse> response = postService.findAll(pageable);
        return ResponseEntity.ok(response);
    }
}
