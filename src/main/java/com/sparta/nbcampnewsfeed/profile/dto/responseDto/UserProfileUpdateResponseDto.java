package com.sparta.nbcampnewsfeed.profile.dto.responseDto;

import com.sparta.nbcampnewsfeed.profile.entity.User;
import lombok.Getter;

@Getter
public class UserProfileUpdateResponseDto {
    private final Long userId;
    private final String username;
    private final String bio;
    private final String email;
    private final boolean active;
    private final String updatedAt;

    // User 엔티티에서 필요한 데이터 추출하여 DTO 생성
    public UserProfileUpdateResponseDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.email = user.getEmail();
        this.active = user.isActive();
        this.updatedAt = user.getUpdatedAt().toString();
    }
}
