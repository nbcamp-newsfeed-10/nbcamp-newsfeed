package com.sparta.nbcampnewsfeed.like.dto.responseDto;

import com.sparta.nbcampnewsfeed.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeCountResponseDto {
    private Long postId;
    private Long likeCount;

    public LikeCountResponseDto(Post post, Long likeCount) {
        this.postId = post.getPostId();
        this.likeCount = likeCount;
    }
}
