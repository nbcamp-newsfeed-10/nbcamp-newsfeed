package com.sparta.nbcampnewsfeed.controller;

import com.sparta.nbcampnewsfeed.annotation.Auth;
import com.sparta.nbcampnewsfeed.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.dto.responseDto.UserProfileMeResponseDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.UserProfileResponseDto;
import com.sparta.nbcampnewsfeed.dto.requestDto.UserProfileUpdateRequestDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.UserProfileUpdateResponseDto;
import com.sparta.nbcampnewsfeed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserProfileController {
    private final UserService userService;

    // 프로필 조회
    @GetMapping("/{user_id}/profile")
    public ResponseEntity<?> getUserProfile(
            @PathVariable Long user_id,
            @Auth AuthUser authUser) {

        if (authUser.getId() != null && authUser.getId().equals(user_id)) {
            // 자신이 자신의 프로필을 조회하는 경우 모든 정보 반환
            UserProfileMeResponseDto responseDto = userService.getUserProfileForMe(user_id);
            return ResponseEntity.ok(responseDto);
        } else {
            // 다른 사용자가 조회하는 경우 민감한 정보를 제외한 정보 반환
            UserProfileResponseDto responseDto = userService.getUserProfile(user_id);
            if (responseDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            return ResponseEntity.ok(responseDto);
        }
    }

    // 프로필 수정
    @PutMapping("/{user_id}/profile")
    public ResponseEntity<UserProfileUpdateResponseDto> updateUserProfile(
            @PathVariable Long user_id,
            @RequestBody UserProfileUpdateRequestDto updateRequest,
            @Auth AuthUser authUser) {

        UserProfileUpdateResponseDto responseDto = userService.updateUserProfile(user_id, updateRequest, authUser);
        if (responseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(responseDto);
    }
}
