package com.sparta.nbcampnewsfeed.controller;

import com.sparta.nbcampnewsfeed.dto.PostRequestDto;
import com.sparta.nbcampnewsfeed.dto.PostResponseDto;
import com.sparta.nbcampnewsfeed.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    public final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // create Post
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @RequestBody PostRequestDto requestDto,
            Authentication authentication) {
        // 인증된 사용자 정보 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());

        // 게시물 생성
        PostResponseDto responseDto = postService.createPost(userId, requestDto);

        return ResponseEntity.ok(responseDto);
    }
}