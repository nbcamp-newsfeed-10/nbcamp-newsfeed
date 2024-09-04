package com.sparta.nbcampnewsfeed.comment.controller;

import com.sparta.nbcampnewsfeed.auth.annotation.Auth;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.comment.dto.requestDto.CommentRequestDto;
import com.sparta.nbcampnewsfeed.comment.dto.requestDto.CommentUpdateRequestDto;
import com.sparta.nbcampnewsfeed.comment.dto.responseDto.CommentDetailResponseDto;
import com.sparta.nbcampnewsfeed.comment.dto.responseDto.CommentSimpleResponseDto;
import com.sparta.nbcampnewsfeed.comment.dto.responseDto.CommentResponseDto;
import com.sparta.nbcampnewsfeed.comment.dto.responseDto.CommentUpdateResponseDto;
import com.sparta.nbcampnewsfeed.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto, @Auth AuthUser authUser) {
        return ResponseEntity.ok(commentService.createComment(commentRequestDto, authUser.getId()));
    }

    //댓글 전체 조회
    @GetMapping
    public ResponseEntity<List<CommentSimpleResponseDto>> getAllComment(@Auth AuthUser authUser) {
        return ResponseEntity.ok(commentService.getAllComment());
    }

    //댓글 단건 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDetailResponseDto> getComment(@PathVariable Long commentId, @Auth AuthUser authUser) {
        return ResponseEntity.ok(commentService.getComment(commentId));
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequestDto commentUpdateRequestDto, @Auth AuthUser authUser) {
        return ResponseEntity.ok(commentService.updateComment(commentId, commentUpdateRequestDto, authUser.getId()));
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId, @Auth AuthUser authUser) {
        commentService.deleteComment(commentId, authUser.getId());
    }
}
