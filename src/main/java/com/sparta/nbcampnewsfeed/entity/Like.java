package com.sparta.nbcampnewsfeed.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "likes") // mysql 예약어여서 꼭 넣어주기
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id") //유저정보도 필요하니
//    private User user;

    @ManyToOne(fetch = FetchType.LAZY) //게시글전용 좋아요 기능
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String username; // 사용자의 이름 (Authentication에서 가져옴)

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}

//@OneToMany(mappedBy = "user") // user1명이 여러 friend 좋아요 누르기
//private List<Like> likes = new ArrayList<>();
//
//@OneToMany(mappedBy = "friend") // 1명의 친구가 여러 user에게 좋아요 받기
//private List<Like> likes = new ArrayList<>();
