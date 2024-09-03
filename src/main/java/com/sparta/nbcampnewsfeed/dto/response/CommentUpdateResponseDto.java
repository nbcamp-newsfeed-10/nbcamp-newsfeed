package com.sparta.nbcampnewsfeed.dto.response;

import com.sparta.nbcampnewsfeed.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class CommentUpdateResponseDto {

    private final Long commentId;
    private final String content;
    private final LocalDateTime updatedAt;

    public CommentUpdateResponseDto(Long commentId, String content, LocalDateTime updatedAt) {
        this.commentId = commentId;
        this.content = content;
        this.updatedAt = updatedAt;
    }

    public CommentUpdateResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.updatedAt = comment.getUpdatedAt();
    }
}
