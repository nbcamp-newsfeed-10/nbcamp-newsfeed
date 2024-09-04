package com.sparta.nbcampnewsfeed.profile.controller;

import com.sparta.nbcampnewsfeed.ApiPayload.ApiResponse;
import com.sparta.nbcampnewsfeed.auth.annotation.Auth;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.profile.dto.requestDto.WithdrawRequestDto;
import com.sparta.nbcampnewsfeed.profile.dto.responseDto.UserProfileMeResponseDto;
import com.sparta.nbcampnewsfeed.profile.dto.responseDto.UserProfileResponseDto;
import com.sparta.nbcampnewsfeed.profile.dto.requestDto.UserProfileUpdateRequestDto;
import com.sparta.nbcampnewsfeed.profile.dto.responseDto.UserProfileUpdateResponseDto;
import com.sparta.nbcampnewsfeed.profile.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // 프로필 조회
    @GetMapping("/{userId}/profile")
    public ApiResponse<?> getUserProfile(
            @PathVariable Long userId,
            @Auth AuthUser authUser) {

        if (authUser.getId() != null && authUser.getId().equals(userId)) {
            // 자신이 자신의 프로필을 조회하는 경우 모든 정보 반환
            UserProfileMeResponseDto responseDto = userService.getUserProfileForMe(userId);
            return ApiResponse.onSuccess(responseDto);
        } else {
            // 다른 사용자가 조회하는 경우 민감한 정보를 제외한 정보 반환
            UserProfileResponseDto responseDto = userService.getUserProfile(userId);
            return ApiResponse.onSuccess(responseDto);
        }
    }

    // 프로필 수정
    @PutMapping("/{userId}/profile")
    public ApiResponse<UserProfileUpdateResponseDto> updateUserProfile(
            @PathVariable Long userId,
            @RequestBody UserProfileUpdateRequestDto updateRequest,
            @Auth AuthUser authUser) {

        UserProfileUpdateResponseDto responseDto = userService.updateUserProfile(userId, updateRequest, authUser);
        return ApiResponse.onSuccess(responseDto);
    }

    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public ApiResponse<String> withdraw(@RequestBody WithdrawRequestDto requestDto,
                           @Auth AuthUser authUser) {
        userService.withdraw(requestDto, authUser);
        return ApiResponse.onSuccess("Withdraw Success");
    }
}
