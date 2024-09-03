package com.sparta.nbcampnewsfeed.dto;

import com.sparta.nbcampnewsfeed.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private final Long postId;
    private final Long userId;
    private final String title;
    private final String content;
    private final LocalDateTime create_at;
    private final LocalDateTime update_at;

    // Post 엔티티를 Dto 로 변환하는 메서드
    public PostResponseDto(Long postId, Long userId, String title, String content, LocalDateTime create_at, LocalDateTime update_at) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.create_at = create_at;
        this.update_at = update_at;
    }
}
