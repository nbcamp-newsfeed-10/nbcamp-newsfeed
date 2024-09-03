package com.sparta.nbcampnewsfeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}

//private Long id;
//
//@Column(nullable = false)
//private String content;
//
//@Column(nullable = false)
//private String username; // 게시물 작성자
//
//@Column(nullable = false)
//private int likeCount = 0;
//
//// 좋아요 수 증가
//public void incrementLike() {
//    this.likeCount++;
//}
//
//// 좋아요 수 감소
//public void decrementLike() {
//    this.likeCount--;
//}