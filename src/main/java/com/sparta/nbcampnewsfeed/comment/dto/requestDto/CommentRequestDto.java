package com.sparta.nbcampnewsfeed.comment.dto.requestDto;

import lombok.Getter;

@Getter
public class CommentRequestDto {

    private Long postId;
    private String content;
}
