package com.sparta.nbcampnewsfeed.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;
    private String user;
}
