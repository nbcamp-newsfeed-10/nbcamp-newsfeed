package com.sparta.nbcampnewsfeed.service;

import com.sparta.nbcampnewsfeed.entity.User;
import com.sparta.nbcampnewsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 프로필 조회
    public User getUserProfile(Long user_id) {
        return userRepository.findById(user_id).orElse(null);
    }

    // 프로필 저장
    public void updateUserProfile(User profile) {
        userRepository.save(profile);
    }
}
