package com.sparta.nbcampnewsfeed.dto.request;

import lombok.Getter;

@Getter
public class CommentRequestDto {

    private Long postId;
    private Long userId;
    private String content;
}
