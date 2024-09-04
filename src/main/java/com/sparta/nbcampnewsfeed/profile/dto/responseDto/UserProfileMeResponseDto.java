package com.sparta.nbcampnewsfeed.profile.dto.responseDto;

import com.sparta.nbcampnewsfeed.profile.entity.User;
import lombok.Getter;

@Getter
public class UserProfileMeResponseDto {
    private final Long userId;
    private final String username;
    private final String email;
    private final String bio;
    private final boolean active;
    private final String createdAt;
    private final String updatedAt;

    // User 엔티티에서 모든 데이터 추출하여 DTO 생성
    public UserProfileMeResponseDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.active = user.isActive();
        this.createdAt = user.getCreatedAt().toString();
        this.updatedAt = user.getUpdatedAt().toString();
    }
}
