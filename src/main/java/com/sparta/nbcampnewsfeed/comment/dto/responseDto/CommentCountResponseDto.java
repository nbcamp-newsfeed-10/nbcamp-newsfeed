package com.sparta.nbcampnewsfeed.comment.dto.responseDto;

import com.sparta.nbcampnewsfeed.comment.entity.Comment;
import com.sparta.nbcampnewsfeed.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCountResponseDto {
    private long postId;
    private long commentCount;

    public CommentCountResponseDto(Post post, Long commentCount) {
        this.postId = post.getPostId();
        this.commentCount = commentCount;
    }
}
