package com.sparta.nbcampnewsfeed.auth.dto.responseDto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponseDto(Long userId, String username, String email, String bio, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserResponseDto of(SignupResponseDto signupResponseDto) {
        return new UserResponseDto(signupResponseDto.getUserId(), signupResponseDto.getUsername(), signupResponseDto.getEmail(),
                signupResponseDto.getBio(), signupResponseDto.getCreatedAt(), signupResponseDto.getUpdatedAt());
    }
}
