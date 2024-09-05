package com.sparta.nbcampnewsfeed.comment.controller;

import com.sparta.nbcampnewsfeed.ApiPayload.ApiResponse;
import com.sparta.nbcampnewsfeed.auth.annotation.Auth;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.comment.dto.requestDto.CommentRequestDto;
import com.sparta.nbcampnewsfeed.comment.dto.requestDto.CommentUpdateRequestDto;
import com.sparta.nbcampnewsfeed.comment.dto.responseDto.*;
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
    public ApiResponse<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto, @Auth AuthUser authUser) {
        CommentResponseDto commentResponseDto = commentService.createComment(commentRequestDto, authUser.getId());
        return ApiResponse.onSuccess(commentResponseDto);
    }

    //특정 게시글에 대한 댓글 전체 조회
    @GetMapping("/post/{postId}")
    public ApiResponse<List<CommentSimpleResponseDto>> getAllComment(@PathVariable Long postId, @Auth AuthUser authUser) {
        List<CommentSimpleResponseDto> comments = commentService.getAllComment(postId);
        return ApiResponse.onSuccess(comments);
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public ApiResponse<CommentUpdateResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequestDto commentUpdateRequestDto, @Auth AuthUser authUser) {
        return ApiResponse.onSuccess(commentService.updateComment(commentId, commentUpdateRequestDto, authUser.getId()));
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ApiResponse<String> deleteComment(@PathVariable Long commentId, @Auth AuthUser authUser) {
        commentService.deleteComment(commentId, authUser.getId());
        return ApiResponse.onSuccess("Comment Delete Success");
    }

    //특정 게시글 댓글 개수 조회
    @GetMapping("/{postId}/count")
    public ApiResponse<CommentCountResponseDto> getCommentCount(@PathVariable Long postId, @Auth AuthUser authUser) {
        // AuthUser에서 userId 추출
        Long userId = authUser.getId();

        // 서비스에 userId 전달
        CommentCountResponseDto commentCountResponseDto = commentService.getCommentCount(postId, userId);
        return ApiResponse.onSuccess(commentCountResponseDto);
    }
}
