package com.sparta.nbcampnewsfeed.controller;

import com.sparta.nbcampnewsfeed.annotation.Auth;
import com.sparta.nbcampnewsfeed.dto.PostRequestDto;
import com.sparta.nbcampnewsfeed.dto.PostResponseDto;
import com.sparta.nbcampnewsfeed.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {
    public final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // create Post
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@Auth AuthUser authUser, @RequestBody PostRequestDto requestDto) {
        System.out.println("authUser=" + authUser.getId());
        PostResponseDto responseDto = postService.createPost(authUser.getId(), requestDto);
        return ResponseEntity.status(201).body(responseDto);
    }
}