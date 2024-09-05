package com.sparta.nbcampnewsfeed.like.cotroller;
import com.sparta.nbcampnewsfeed.ApiPayload.ApiResponse;
import com.sparta.nbcampnewsfeed.auth.annotation.Auth;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.like.dto.responseDto.LikePostResponseDto;
import com.sparta.nbcampnewsfeed.like.service.LikeService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/likes")
public class LikeController {
    private final LikeService likeService;
    //좋아요 생성
    @PostMapping
    public ApiResponse<LikePostResponseDto> likePost(@PathVariable Long postId, @Auth AuthUser authUser) {
        LikePostResponseDto responseDto = likeService.likePost(postId, authUser);
        return ApiResponse.onSuccess(responseDto);
    }
    //좋아요 취소
    @DeleteMapping
    public ApiResponse<String> unlikePost(@PathVariable Long postId, @Auth AuthUser authUser) {
        likeService.unlikePost(postId, authUser);
        return ApiResponse.onSuccess("Unlike Post Success");
    }
}
