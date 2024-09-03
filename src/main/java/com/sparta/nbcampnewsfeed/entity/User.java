package com.sparta.nbcampnewsfeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;
    private String email;
    private String bio;
    private String password;
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
  
    // 비밀번호 변경 메소드
    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    // 프로필 수정 메소드
    public void updateProfile(String newUsername, String newBio) {
        this.username = newUsername;
        this.bio = newBio;
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor for JPA
    protected User() {}

    // 생성자를 통해 필수 값 설정
    public User(String username, String email, String bio, String password) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.password = password;
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
