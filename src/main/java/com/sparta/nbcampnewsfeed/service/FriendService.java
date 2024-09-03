package com.sparta.nbcampnewsfeed.service;

import com.sparta.nbcampnewsfeed.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.dto.responseDto.UserProfileResponseDto;
import com.sparta.nbcampnewsfeed.entity.Friend;
import com.sparta.nbcampnewsfeed.entity.User;
import com.sparta.nbcampnewsfeed.repository.FriendRepository;
import com.sparta.nbcampnewsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserProfileResponseDto friendRequest(Long userId, AuthUser authUser) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User fromUser = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 탈퇴한 회원에게 친구 신청 불가
        if (!user.isActive()) {
            throw new IllegalArgumentException("withdraw user");
        }

        // 자기 자신에게 친구 신청 불가
        if (userId.equals(authUser.getId())) {
            throw new IllegalArgumentException();
        }

        // 이미 친구인 경우 or 친구 신청을 한 경우 다시 친구 신청 불가
        friendRepository.findByToUserAndFromUser(user, fromUser)
                .ifPresent(u -> {
                    throw new IllegalArgumentException("이미 친구이거나 친구 신청을 한 회원입니다.");});

        Friend friend = new Friend(user, fromUser);
        friendRepository.save(friend);

        return new UserProfileResponseDto(user);
    }


}
