package com.sparta.nbcampnewsfeed.friend.controller;

import com.sparta.nbcampnewsfeed.ApiPayload.ApiResponse;
import com.sparta.nbcampnewsfeed.auth.annotation.Auth;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    // 친구 신청 api
    @PostMapping("/{userId}")
    public ApiResponse<String> friendRequest(@Auth AuthUser authUser, @PathVariable Long userId) {
        friendService.friendRequest(userId, authUser);
        return ApiResponse.onSuccess("Friend Request Success");
    }

    // 친구 수락 api
    @PostMapping("/{userId}/accept")
    public ApiResponse<String> acceptFriendRequest(@Auth AuthUser authUser, @PathVariable Long userId) {
        friendService.acceptFriendRequest(userId, authUser);
        return ApiResponse.onSuccess("Friend Request Accept Success");
    }

    // 친구 삭제 api
    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteFriend(@Auth AuthUser authUser, @PathVariable Long userId) {
        friendService.deleteFriend(userId, authUser);
        return ApiResponse.onSuccess("Friend Delete Success");
    }
}
