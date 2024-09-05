package com.sparta.nbcampnewsfeed.comment.dto.responseDto;

import com.sparta.nbcampnewsfeed.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentSimpleResponseDto {

    private final Long commentId;
    private final String content;
    private final Long userId;

    //Comment 를 매개변수로 받은 생성자
    public CommentSimpleResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.userId = comment.getUser().getUserId();
    }
}