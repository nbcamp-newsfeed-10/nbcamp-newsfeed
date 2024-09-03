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

    @Transactional
    public UserProfileResponseDto acceptFriendRequest(Long userId, AuthUser authUser) {
        User fromUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User toUser = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 탈퇴한 회원의 친구 신청 수락 불가
        if (!fromUser.isActive()) {
            throw new IllegalArgumentException("withdraw user");
        }

        // 친구 요청 기록이 없는 경우 수락 불가
        Friend friend = friendRepository.findByToUserAndFromUser(toUser, fromUser)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원에게 받은 친구 추가 요청이 없습니다"));

        // 이미 친구 관계인 경우
        friendRepository.findByToUserAndFromUserAndAndFriendStatus(toUser, fromUser, true)
                .ifPresent(u -> {throw new IllegalArgumentException("이미 친구인 회원입니다.");});

        // 친구 관계 true 로 설정
        friend.acceptFriend();

        // 양방향 친구 관계 생성 & 저장(게시물의 원활한 조회를 위해)
        Friend newFriend = new Friend(fromUser, toUser);
        newFriend.acceptFriend();
        friendRepository.save(newFriend);

        return new UserProfileResponseDto(fromUser);
    }

}
