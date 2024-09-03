package com.sparta.nbcampnewsfeed.likeRepository;

import com.sparta.nbcampnewsfeed.entity.Like;
import com.sparta.nbcampnewsfeed.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPost_idAndUsername(Long post_id, String username);
    void deleteByPost_idAndUsername(Long post_id, String username);
}

//public interface PostRepository extends JpaRepository<Post, Long> {
//    Optional<Post> findById(Long id);
//}

