package com.sparta.nbcampnewsfeed.likeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikePostResponseDto {
    private Long post_id;
    private int likeCount;

    public LikePostResponseDto(Long post_id, int likeCount) {
        this.post_id = post_id;
        this.likeCount = likeCount;
    }
}
