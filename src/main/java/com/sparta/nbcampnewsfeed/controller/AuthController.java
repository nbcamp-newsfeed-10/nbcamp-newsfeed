package com.sparta.nbcampnewsfeed.controller;

import com.sparta.nbcampnewsfeed.dto.requestDto.SigninRequest;
import com.sparta.nbcampnewsfeed.dto.requestDto.SignupRequestDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.SignupResponseDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.UserResponseDto;
import com.sparta.nbcampnewsfeed.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody SignupRequestDto signUpRequestDto) {

        SignupResponseDto signupResponseDto = userService.save(signUpRequestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", signupResponseDto.getBearerToken());
        return new ResponseEntity<>(UserResponseDto.of(signupResponseDto),
                headers, HttpStatus.OK);
    }

    // 로그인
    @GetMapping("/signin")
    public ResponseEntity<Void> signin(@RequestBody SigninRequest signinRequest) {
        String bearerToken = userService.signin(signinRequest);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .build();
    }
}
