package com.sparta.nbcampnewsfeed.controller;

import com.sparta.nbcampnewsfeed.dto.SignupRequestDto;
import com.sparta.nbcampnewsfeed.dto.SignupResponseDto;
import com.sparta.nbcampnewsfeed.dto.UserResponseDto;
import com.sparta.nbcampnewsfeed.service.UserService;
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
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody SignupRequestDto signUpRequestDto) {

        SignupResponseDto signupResponseDto = userService.save(signUpRequestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", signupResponseDto.getBearerToken());
        return new ResponseEntity<>(UserResponseDto.of(signupResponseDto),
                headers, HttpStatus.OK);
    }
}
