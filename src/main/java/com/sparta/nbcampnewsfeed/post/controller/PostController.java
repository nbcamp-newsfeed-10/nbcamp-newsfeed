package com.sparta.nbcampnewsfeed.post.controller;

import com.sparta.nbcampnewsfeed.auth.annotation.Auth;
import com.sparta.nbcampnewsfeed.post.dto.requestDto.PostRequestDto;
import com.sparta.nbcampnewsfeed.post.dto.responseDto.PostResponseDto;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    public final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시물 작성
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@Auth AuthUser authUser, @RequestBody PostRequestDto requestDto) {
        System.out.println("authUser=" + authUser.getId());
        PostResponseDto responseDto = postService.createPost(authUser.getId(), requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 게시물 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@Auth AuthUser authUser, @PathVariable Long postId, @RequestBody PostRequestDto requestDto) {
        PostResponseDto responseDto = postService.updatePost(authUser.getId(), postId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 게시물 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@Auth AuthUser authUser, @PathVariable Long postId) {
        PostResponseDto responseDto = postService.getPost(authUser.getId(), postId);
        return ResponseEntity.ok(responseDto);
    }

    // 게시물 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@Auth AuthUser authUser, @PathVariable Long postId) {
        postService.deletePost(authUser.getId(), postId);
        return ResponseEntity.noContent().build();
    }

    // 뉴스피드 조회
    @GetMapping("/newsfeed")
    public ResponseEntity<List<PostResponseDto>> getNewsFeed(@Auth AuthUser authUser,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        List<PostResponseDto> newsfeed = postService.getNewsfeed(authUser.getId(), page - 1, size);
        return ResponseEntity.ok(newsfeed);
    }
}