package com.sparta.nbcampnewsfeed.repository;

import com.sparta.nbcampnewsfeed.entity.Like;
import com.sparta.nbcampnewsfeed.entity.Post;
import com.sparta.nbcampnewsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser (Post post, User user);
}

