package com.sparta.nbcampnewsfeed.controller;

import com.sparta.nbcampnewsfeed.annotation.Auth;
import com.sparta.nbcampnewsfeed.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    // 친구 신청 api
    @PostMapping("/{userId}")
    public String friendRequest(@Auth AuthUser authUser, @PathVariable Long userId) {
        friendService.friendRequest(userId, authUser);
        return "ok";
    }

    // 친구 수락 api
    @PostMapping("/{userId}/accept")
    public String acceptFriendRequest(@Auth AuthUser authUser, @PathVariable Long userId) {
        friendService.acceptFriendRequest(userId, authUser);
        return "ok";
    }

    // 친구 삭제 api
    @DeleteMapping("/{userId}")
    public String deleteFriend(@Auth AuthUser authUser, @PathVariable Long userId) {
        friendService.deleteFriend(userId, authUser);
        return "ok";
    }
}
