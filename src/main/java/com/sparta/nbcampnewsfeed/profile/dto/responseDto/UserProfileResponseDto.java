package com.sparta.nbcampnewsfeed.profile.dto.responseDto;

import com.sparta.nbcampnewsfeed.profile.entity.User;
import lombok.Getter;

@Getter
public class UserProfileResponseDto {
    private final Long userId;
    private final String username;
    private final String bio;
    private final boolean active;

    // User 엔티티에서 필요한 데이터만 추출하여 DTO 생성
    public UserProfileResponseDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.active = user.isActive();
    }
}
