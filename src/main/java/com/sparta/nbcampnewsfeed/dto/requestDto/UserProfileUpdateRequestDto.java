package com.sparta.nbcampnewsfeed.dto.requestDto;

import lombok.Getter;

@Getter
public class UserProfileUpdateRequestDto {
    private String username;
    private String bio;
    private String currentPassword;
    private String newPassword;
}
