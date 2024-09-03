package com.sparta.nbcampnewsfeed.service;

import com.sparta.nbcampnewsfeed.config.JwtUtil;
import com.sparta.nbcampnewsfeed.config.PasswordEncoder;
import com.sparta.nbcampnewsfeed.dto.requestDto.SigninRequest;
import com.sparta.nbcampnewsfeed.dto.requestDto.SignupRequestDto;
import com.sparta.nbcampnewsfeed.dto.requestDto.UserProfileUpdateRequestDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.SignupResponseDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.UserProfileMeResponseDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.UserProfileResponseDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.UserProfileUpdateResponseDto;
import com.sparta.nbcampnewsfeed.entity.User;
import com.sparta.nbcampnewsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponseDto save(SignupRequestDto signUpRequestDto) {

        // 중복된 email 가입 시도시
        userRepository.findByEmail(signUpRequestDto.getEmail())
                .ifPresent(u -> {throw new IllegalArgumentException();});

        User user = new User(signUpRequestDto.getUsername(), signUpRequestDto.getEmail(), signUpRequestDto.getBio(),
                passwordEncoder.encode(signUpRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        String token = jwtUtil.createToken(savedUser.getUserId(), savedUser.getEmail());
        return SignupResponseDto.of(savedUser, token);
    }

    public String signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 비밀번호 불일치
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("wrong password");
        }

        return jwtUtil.createToken(user.getUserId(), user.getEmail());
    }

    public UserProfileMeResponseDto getUserProfileForMe(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        return new UserProfileMeResponseDto(user);
    }

    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        return new UserProfileResponseDto(user);
    }

    @Transactional
    public UserProfileUpdateResponseDto updateUserProfile(Long userId, UserProfileUpdateRequestDto updateRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        // 비밀번호 변경 처리
        if (updateRequest.getCurrentPassword() != null && updateRequest.getNewPassword() != null) {
            if (!updateRequest.getCurrentPassword().equals(user.getPassword())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }

            if (updateRequest.getCurrentPassword().equals(updateRequest.getNewPassword())) {
                throw new IllegalArgumentException("New password cannot be the same as the current password");
            }

            if (!isValidPassword(updateRequest.getNewPassword())) {
                throw new IllegalArgumentException("New password does not meet the required format");
            }

            user.changePassword(updateRequest.getNewPassword());
        }

        // 프로필 정보 수정 처리
        user.updateProfile(updateRequest.getUsername(), updateRequest.getBio());
        userRepository.save(user);

        return new UserProfileUpdateResponseDto(user);
    }

    private boolean isValidPassword(String password) {
        // 최소 8자, 대소문자 포함 영문, 숫자, 특수문자 포함 형식 검증
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+])[A-Za-z\\d~!@#$%^&*()_+]{8,}$";
        return password.matches(passwordPattern);
    }
}
