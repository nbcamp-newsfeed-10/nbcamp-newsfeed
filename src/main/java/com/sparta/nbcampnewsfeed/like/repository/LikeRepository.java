package com.sparta.nbcampnewsfeed.like.repository;

import com.sparta.nbcampnewsfeed.like.entity.Like;
import com.sparta.nbcampnewsfeed.post.entity.Post;
import com.sparta.nbcampnewsfeed.profile.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser (Post post, User user);

    // 특정 게시물의 좋아요 개수를 카운트하는 메서드
    // 좋아요 개수는 0아니면 정수니까 null 이 들어올 수 있는 가능성이 없어서 optional 안넣어도됨
    Long countByPost(Post post);
}

