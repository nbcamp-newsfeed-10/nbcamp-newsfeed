package com.sparta.nbcampnewsfeed.likeController;
import com.sparta.nbcampnewsfeed.annotation.Auth;
import com.sparta.nbcampnewsfeed.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.entity.User;
import com.sparta.nbcampnewsfeed.likeDto.LikePostResponseDto;
import com.sparta.nbcampnewsfeed.likeService.LikeService;
import com.sparta.nbcampnewsfeed.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/likes")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<LikePostResponseDto> likePost(@PathVariable Long postId, @Auth AuthUser authUser) {
        LikePostResponseDto responseDto = likeService.likePost(postId, authUser);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/posts/{postId}/likes")
    public String unlikePost(@PathVariable Long postId, @Auth AuthUser authUser) {
        likeService.unlikePost(postId, authUser);
        return "ok";
    }
}
