package com.sparta.nbcampnewsfeed.controller;

import com.sparta.nbcampnewsfeed.dto.request.CommentRequestDto;
import com.sparta.nbcampnewsfeed.dto.response.CommentResponseDto;
import com.sparta.nbcampnewsfeed.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(commentService.createComment(commentRequestDto));
    }

    @GetMapping("/comments")
    public
}
