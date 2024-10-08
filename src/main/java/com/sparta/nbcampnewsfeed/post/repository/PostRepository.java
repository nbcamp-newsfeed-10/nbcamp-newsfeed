package com.sparta.nbcampnewsfeed.post.repository;

import com.sparta.nbcampnewsfeed.post.entity.Post;
import com.sparta.nbcampnewsfeed.profile.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 친구들의 게시물을 가져오기 위한 쿼리 (내림차순 정렬)
    Page<Post> findAllByUserUserIdInOrderByCreatedAtDesc(List<Long> userIds, Pageable pageable);

    List<Post> findAllByUser(User user);
}