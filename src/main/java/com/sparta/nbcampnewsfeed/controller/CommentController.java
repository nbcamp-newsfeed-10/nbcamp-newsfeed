package com.sparta.nbcampnewsfeed.controller;

import com.sparta.nbcampnewsfeed.dto.requestDto.CommentRequestDto;
import com.sparta.nbcampnewsfeed.dto.requestDto.CommentUpdateRequestDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.CommentDetailResponseDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.CommentSimpleResponseDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.CommentResponseDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.CommentUpdateResponseDto;
import com.sparta.nbcampnewsfeed.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(commentService.createComment(commentRequestDto));
    }

    //댓글 전체 조회
    @GetMapping("/comments")
    public ResponseEntity<List<CommentSimpleResponseDto>> getAllComment() {
        return ResponseEntity.ok(commentService.getAllComment());
    }

    //댓글 단건 조회
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDetailResponseDto> getComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getComment(commentId));
    }

    //댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequestDto commentUpdateRequestDto) {
        return ResponseEntity.ok(commentService.updateComment(commentId, commentUpdateRequestDto));
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
