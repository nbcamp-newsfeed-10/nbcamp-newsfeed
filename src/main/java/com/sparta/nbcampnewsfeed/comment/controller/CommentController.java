package com.sparta.nbcampnewsfeed.comment.controller;

import com.sparta.nbcampnewsfeed.ApiPayload.ApiResponse;
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
    public ApiResponse<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto, @Auth AuthUser authUser) {
        CommentResponseDto commentResponseDto = commentService.createComment(commentRequestDto, authUser.getId());
        return ApiResponse.onSuccess(commentResponseDto);
    }

    //댓글 전체 조회
    @GetMapping
    public ApiResponse<List<CommentSimpleResponseDto>> getAllComment(@Auth AuthUser authUser) {
        return ApiResponse.onSuccess(commentService.getAllComment());
    }

    //댓글 단건 조회
    @GetMapping("/{commentId}")
    public ApiResponse<CommentDetailResponseDto> getComment(@PathVariable Long commentId, @Auth AuthUser authUser) {
        return ApiResponse.onSuccess(commentService.getComment(commentId));
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
}
