package com.sparta.nbcampnewsfeed.service;

import com.sparta.nbcampnewsfeed.config.JwtUtil;
import com.sparta.nbcampnewsfeed.config.PasswordEncoder;
import com.sparta.nbcampnewsfeed.dto.SignupRequestDto;
import com.sparta.nbcampnewsfeed.dto.SignupResponseDto;
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
        String token = jwtUtil.createToken(savedUser);
        return SignupResponseDto.of(savedUser, token);
    }
    
    // 프로필 조회
    public User getUserProfile(Long user_id) {
        return userRepository.findById(user_id).orElse(null);
    }

    // 프로필 저장
    public void updateUserProfile(User profile) {
        userRepository.save(profile);
}
