package com.sparta.nbcampnewsfeed.auth.dto.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @NotBlank
    @Size(max=20)
    private String username;
    @Email
    private String email;
    private String bio;
    @Pattern(message = "최소 8글자 이상, 영문 + 숫자 + 특수문자를 최소 1글자씩 포함",
            regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
    private String password;
}
