package com.sparta.nbcampnewsfeed.controller;

import com.sparta.nbcampnewsfeed.annotation.Auth;
import com.sparta.nbcampnewsfeed.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.dto.responseDto.UserProfileResponseDto;
import com.sparta.nbcampnewsfeed.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    // 친구 신청 api
    @PostMapping("/{userId}")
    public UserProfileResponseDto friendRequest(@Auth AuthUser authUser,
                                                @PathVariable Long userId) {
        return friendService.friendRequest(userId, authUser);
    }

    // 친구 수락 api
    @PostMapping("/{userId}/accept")
    public UserProfileResponseDto acceptFriendRequest(@Auth AuthUser authUser,
                                                      @PathVariable Long userId) {
        return friendService.acceptFriendRequest(userId, authUser);
    }
}
