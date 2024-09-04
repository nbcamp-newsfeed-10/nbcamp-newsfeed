package com.sparta.nbcampnewsfeed.dto.responseDto;

import com.sparta.nbcampnewsfeed.entity.Comment;
import lombok.Getter;

@Getter
public class CommentSimpleResponseDto {

    private final Long commentId;
    private final String content;
    private final Long userId;

    public CommentSimpleResponseDto(Long commentId, String content, Long userId) {
        this.commentId = commentId;
        this.content = content;
        this.userId = userId;
    }

    //Comment를 매개변수로 받은 생성자
    public CommentSimpleResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.userId = comment.getUser().getUserId();
    }
}