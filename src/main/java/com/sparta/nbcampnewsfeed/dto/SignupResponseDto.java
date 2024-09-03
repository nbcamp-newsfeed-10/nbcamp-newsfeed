package com.sparta.nbcampnewsfeed.dto;

import com.sparta.nbcampnewsfeed.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class SignupResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String bio;
    @Setter
    private String bearerToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SignupResponseDto(Long userId, String username, String email, String bio,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SignupResponseDto of(User user, String bearerToken) {
        SignupResponseDto responseDto = new SignupResponseDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getBio(), user.getCreatedAt(), user.getUpdatedAt());
        responseDto.setBearerToken(bearerToken);
        return responseDto;
    }
}
