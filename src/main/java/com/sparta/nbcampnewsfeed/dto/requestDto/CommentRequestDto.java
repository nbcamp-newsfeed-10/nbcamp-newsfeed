package com.sparta.nbcampnewsfeed.dto.requestDto;

import lombok.Getter;

@Getter
public class CommentRequestDto {

    private Long postId;
    private Long userId;
    private String content;
}
