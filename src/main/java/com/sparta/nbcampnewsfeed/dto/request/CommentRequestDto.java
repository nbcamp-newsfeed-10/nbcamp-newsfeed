package com.sparta.nbcampnewsfeed.dto.request;

import lombok.Getter;

@Getter
public class CommentRequestDto {

    private Long id;
    private String content;
    private String user;
}
