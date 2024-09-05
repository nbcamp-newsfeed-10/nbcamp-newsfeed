package com.sparta.nbcampnewsfeed.profile.dto.requestDto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserProfileUpdateRequestDto {
    private String username;
    private String bio;
    private String currentPassword;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "비밀번호는 최소 8글자 이상, 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다.")
    private String newPassword;
}
