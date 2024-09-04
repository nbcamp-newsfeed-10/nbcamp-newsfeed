package com.sparta.nbcampnewsfeed.dto.responseDto;

import com.sparta.nbcampnewsfeed.entity.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikePostResponseDto {
    private Long postId;
    private Long userId;

    public LikePostResponseDto(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public LikePostResponseDto(Like like) {
        this.postId = like.getPost().getPostId();
        this.userId = like.getUser().getUserId();
    }

}
