package com.sparta.nbcampnewsfeed.friend.service;

import com.sparta.nbcampnewsfeed.ApiPayload.Code.Status.ErrorStatus;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.exception.ApiException;
import com.sparta.nbcampnewsfeed.friend.entity.Friend;
import com.sparta.nbcampnewsfeed.profile.entity.User;
import com.sparta.nbcampnewsfeed.friend.repository.FriendRepository;
import com.sparta.nbcampnewsfeed.profile.repository.UserRepository;
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
    public void friendRequest(Long userId, AuthUser authUser) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));
        User fromUser = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));

        // 탈퇴한 회원에게 친구 신청 불가
        if (!user.isActive()) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_USER);
        }

        // 자기 자신에게 친구 신청 불가
        if (userId.equals(authUser.getId())) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_SELF_FRIEND);
        }

        // 이미 친구인 경우 or 친구 신청을 한 경우 다시 친구 신청 불가
        friendRepository.findByToUserAndFromUser(user, fromUser)
                .ifPresent(u -> {
                    throw new ApiException(ErrorStatus._BAD_REQUEST_FROM_USER);});

        // 이미 친구 신청을 받은 경우 친구 신청 불가
        friendRepository.findByToUserAndFromUser(fromUser, user)
                .ifPresent(u -> {
                    throw new ApiException(ErrorStatus._BAD_REQUEST_TO_USER);});

        Friend friend = new Friend(user, fromUser);
        friendRepository.save(friend);
    }

    @Transactional
    public void acceptFriendRequest(Long userId, AuthUser authUser) {
        User fromUser = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));
        User toUser = userRepository.findById(authUser.getId()).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));

        // 탈퇴한 회원의 친구 신청 수락 불가
        if (!fromUser.isActive()) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_USER);
        }

        // 친구 요청 기록이 없는 경우 수락 불가
        Friend friend = friendRepository.findByToUserAndFromUser(toUser, fromUser)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_FRIEND));

        // 이미 친구 관계인 경우
        friendRepository.findByToUserAndFromUserAndFriendStatus(toUser, fromUser, true)
                .ifPresent(u -> {throw new ApiException(ErrorStatus._BAD_REQUEST_ALREADY_FRIEND);});

        // 친구 관계 true 로 설정
        friend.acceptFriend();

        // 양방향 친구 관계 생성 & 저장(게시물의 원활한 조회를 위해)
        Friend newFriend = new Friend(fromUser, toUser);
        newFriend.acceptFriend();
        friendRepository.save(newFriend);
    }

    @Transactional
    public void deleteFriend(Long userId, AuthUser authUser) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));
        User deleteUser = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));

        // 친구가 아닐 때 삭제를 시도한 경우
        Friend friend1 = friendRepository.findByToUserAndFromUserAndFriendStatus(user, deleteUser, true)
                .orElseThrow(() -> new ApiException(ErrorStatus._BAD_REQUEST_NOT_FRIEND));
        Friend friend2 = friendRepository.findByToUserAndFromUserAndFriendStatus(deleteUser, user, true)
                .orElseThrow(() -> new ApiException(ErrorStatus._BAD_REQUEST_NOT_FRIEND));

        // 친구 관계 삭제
        friendRepository.delete(friend1);
        friendRepository.delete(friend2);
    }

}
