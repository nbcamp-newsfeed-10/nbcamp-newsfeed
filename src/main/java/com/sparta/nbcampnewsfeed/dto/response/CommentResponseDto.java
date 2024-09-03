package com.sparta.nbcampnewsfeed.dto.response;

import com.sparta.nbcampnewsfeed.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long commentId;
    private final Long postId;
    private final Long userId;
    private final String content;
    private final LocalDateTime createdAt;

    public CommentResponseDto(Long commentId, Long postId, Long userId, String content, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.postId = comment.getPost().getPostId();
        this.userId = comment.getUser().getUserId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
