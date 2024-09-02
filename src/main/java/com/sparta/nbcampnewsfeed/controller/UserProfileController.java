package com.sparta.nbcampnewsfeed.controller;

import com.sparta.nbcampnewsfeed.dto.responseDto.UserProfileMeResponseDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.UserProfileResponseDto;
import com.sparta.nbcampnewsfeed.dto.requestDto.UserProfileUpdateRequestDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.UserProfileUpdateResponseDto;
import com.sparta.nbcampnewsfeed.entity.User;
import com.sparta.nbcampnewsfeed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserProfileController {
    private UserService userService;

    // 프로필 조회
    @GetMapping("/{user_id}/profile")
    public ResponseEntity<?> getUserProfile(
            @PathVariable Long user_id,
            @RequestParam(required = false) Long viewer_id) {

        User profile = userService.getUserProfile(user_id);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // 자신이 자신의 프로필을 조회하는 경우 모든 정보 반환
        if (viewer_id != null && viewer_id.equals(user_id)) {
            UserProfileMeResponseDto responseDTO = new UserProfileMeResponseDto(profile);
            return ResponseEntity.ok(responseDTO);
        }

        // 다른 사용자가 조회하는 경우 민감한 정보를 제외한 정보 반환
        UserProfileResponseDto responseDTO = new UserProfileResponseDto(profile);
        return ResponseEntity.ok(responseDTO);
    }

    // 프로필 수정
    @PutMapping("/{user_id}/profile")
    public ResponseEntity<UserProfileUpdateResponseDto> updateUserProfile(
            @PathVariable Long user_id,
            @RequestBody UserProfileUpdateRequestDto updateRequest) {

        User profile = userService.getUserProfile(user_id);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 비밀번호 변경 요청 시 처리
        if (updateRequest.getCurrentPassword() != null && updateRequest.getNewPassword() != null) {
            if (!updateRequest.getCurrentPassword().equals(profile.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            if (updateRequest.getCurrentPassword().equals(updateRequest.getNewPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            if (!isValidPassword(updateRequest.getNewPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            profile.changePassword(updateRequest.getNewPassword());
        }

        // 프로필 정보 수정
        profile.updateProfile(updateRequest.getUsername(), updateRequest.getBio());
        userService.updateUserProfile(profile);

        // 업데이트된 프로필 정보를 DTO로 반환
        UserProfileUpdateResponseDto responseDTO = new UserProfileUpdateResponseDto(profile);
        return ResponseEntity.ok(responseDTO);
    }

    private boolean isValidPassword(String password) {
        // 최소 8자, 대소문자 포함 영문, 숫자, 특수문자 포함 형식 검증
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+])[A-Za-z\\d~!@#$%^&*()_+]{8,}$";
        return password.matches(passwordPattern);
    }
}
