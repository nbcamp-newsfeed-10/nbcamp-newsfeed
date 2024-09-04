package com.sparta.nbcampnewsfeed.like.repository;

import com.sparta.nbcampnewsfeed.like.entity.Like;
import com.sparta.nbcampnewsfeed.post.entity.Post;
import com.sparta.nbcampnewsfeed.profile.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser (Post post, User user);
}

