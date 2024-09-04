package com.sparta.nbcampnewsfeed.dto.responseDto;

import com.sparta.nbcampnewsfeed.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentUpdateResponseDto {

    private final Long commentId;
    private final String content;
    private final LocalDateTime updatedAt;

    public CommentUpdateResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.updatedAt = comment.getUpdatedAt();
    }
}
