package com.sparta.nbcampnewsfeed.comment.repository;

import com.sparta.nbcampnewsfeed.comment.entity.Comment;
import com.sparta.nbcampnewsfeed.profile.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUser(User user);
}
