package com.sparta.nbcampnewsfeed.likeController;
import com.sparta.nbcampnewsfeed.entity.User;
import com.sparta.nbcampnewsfeed.likeDto.LikePostResponseDto;
import com.sparta.nbcampnewsfeed.likeService.LikeService;
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
    public ResponseEntity<LikePostResponseDto> likePost(@PathVariable Long post_id, Authentication authentication) {
        String username = authentication.getName();
        LikePostResponseDto responseDto = likeService.likePost(post_id, username);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/posts/{postId}/likes")
    public ResponseEntity<LikePostResponseDto> unlikePost(@PathVariable Long post_id, Authentication authentication) {
        String username = authentication.getName();
        LikePostResponseDto responseDto = likeService.unlikePost(post_id, username);
        return ResponseEntity.ok(responseDto);
    }
}
