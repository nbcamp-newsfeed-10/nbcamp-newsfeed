package com.sparta.nbcampnewsfeed.like.dto.responseDto;

import com.sparta.nbcampnewsfeed.like.entity.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikePostResponseDto {
    private Long postId;
    private Long userId;

    public LikePostResponseDto(Like like) {
        this.postId = like.getPost().getPostId();
        this.userId = like.getUser().getUserId();
    }
}
