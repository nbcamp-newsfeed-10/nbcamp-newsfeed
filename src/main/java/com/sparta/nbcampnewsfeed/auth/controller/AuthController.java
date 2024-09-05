package com.sparta.nbcampnewsfeed.auth.controller;

import com.sparta.nbcampnewsfeed.ApiPayload.ApiResponse;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.SigninRequest;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.SignupRequestDto;
import com.sparta.nbcampnewsfeed.auth.dto.responseDto.SignupResponseDto;
import com.sparta.nbcampnewsfeed.auth.dto.responseDto.UserResponseDto;
import com.sparta.nbcampnewsfeed.profile.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    // 회원가입 처리
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> signup(@Valid @RequestBody SignupRequestDto signUpRequestDto) {

        SignupResponseDto signupResponseDto = userService.save(signUpRequestDto);
        // header 에 token 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, signupResponseDto.getBearerToken());
        // 응답 dto 생성
        UserResponseDto userResponseDto = UserResponseDto.of(signupResponseDto);
        return new ResponseEntity<>(ApiResponse.onSuccess(userResponseDto),
                headers, HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<String>> signin(@RequestBody SigninRequest signinRequest) {
        String bearerToken = userService.signin(signinRequest);
        // header 에 token 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, bearerToken);

        return new ResponseEntity<>(ApiResponse.onSuccess("Login Success"), headers, HttpStatus.OK);
    }
}
