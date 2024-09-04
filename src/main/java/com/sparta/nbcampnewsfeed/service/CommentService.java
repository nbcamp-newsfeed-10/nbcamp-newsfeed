package com.sparta.nbcampnewsfeed.service;

import com.sparta.nbcampnewsfeed.dto.requestDto.CommentRequestDto;
import com.sparta.nbcampnewsfeed.dto.requestDto.CommentUpdateRequestDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.CommentDetailResponseDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.CommentResponseDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.CommentSimpleResponseDto;
import com.sparta.nbcampnewsfeed.dto.responseDto.CommentUpdateResponseDto;
import com.sparta.nbcampnewsfeed.entity.Comment;
import com.sparta.nbcampnewsfeed.entity.Post;
import com.sparta.nbcampnewsfeed.entity.User;
import com.sparta.nbcampnewsfeed.repository.CommentRepository;
import com.sparta.nbcampnewsfeed.repository.PostRepository;
import com.sparta.nbcampnewsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto) {
        User user = userRepository.findById(commentRequestDto.getUserId()).orElseThrow(() -> new NullPointerException("User ID not found"));
        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(() -> new NullPointerException("Post ID not found"));

        Comment comment = new Comment(post, user, commentRequestDto.getContent());
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public List<CommentSimpleResponseDto> getAllComment() {
        return commentRepository.findAll().stream().map(comment -> new CommentSimpleResponseDto(comment)).toList();
    }

    public CommentDetailResponseDto getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("ID not found"));
        return new CommentDetailResponseDto(comment);
    }

    @Transactional
    public CommentUpdateResponseDto updateComment(Long commentId, CommentUpdateRequestDto commentUpdateRequestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("ID not found"));

        comment.updateContent(commentUpdateRequestDto.getContent());
        return new CommentUpdateResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("ID not found"));
        commentRepository.delete(comment);
    }

    @Transactional
    public void deleteAllComment(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Comment> comments = commentRepository.findAllByUser(user);
        commentRepository.deleteAll(comments);
    }
}
