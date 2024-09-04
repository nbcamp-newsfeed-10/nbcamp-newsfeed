package com.sparta.nbcampnewsfeed.comment.service;

import com.sparta.nbcampnewsfeed.ApiPayload.Code.Status.ErrorStatus;
import com.sparta.nbcampnewsfeed.comment.dto.requestDto.CommentRequestDto;
import com.sparta.nbcampnewsfeed.comment.dto.requestDto.CommentUpdateRequestDto;
import com.sparta.nbcampnewsfeed.comment.dto.responseDto.CommentDetailResponseDto;
import com.sparta.nbcampnewsfeed.comment.dto.responseDto.CommentResponseDto;
import com.sparta.nbcampnewsfeed.comment.dto.responseDto.CommentSimpleResponseDto;
import com.sparta.nbcampnewsfeed.comment.dto.responseDto.CommentUpdateResponseDto;
import com.sparta.nbcampnewsfeed.comment.entity.Comment;
import com.sparta.nbcampnewsfeed.exception.ApiException;
import com.sparta.nbcampnewsfeed.post.entity.Post;
import com.sparta.nbcampnewsfeed.profile.entity.User;
import com.sparta.nbcampnewsfeed.comment.repository.CommentRepository;
import com.sparta.nbcampnewsfeed.post.repository.PostRepository;
import com.sparta.nbcampnewsfeed.profile.repository.UserRepository;
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
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long requesterId) {
        User user = userRepository.findById(commentRequestDto.getUserId()).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));
        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_POST));

        Comment comment = new Comment(post, user, commentRequestDto.getContent());
        commentRepository.save(comment);

        Long commentAuthorId = comment.getUser().getUserId();

        if (!requesterId.equals(commentAuthorId)) {
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }

        return new CommentResponseDto(comment);
    }

    public List<CommentSimpleResponseDto> getAllComment() {
        return commentRepository.findAll().stream().map(comment -> new CommentSimpleResponseDto(comment)).toList();
    }

    public CommentDetailResponseDto getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_COMMENT));
        return new CommentDetailResponseDto(comment);
    }

    @Transactional
    public CommentUpdateResponseDto updateComment(Long commentId, CommentUpdateRequestDto commentUpdateRequestDto, Long requesterId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_COMMENT));


        Long commentAuthorId = comment.getUser().getUserId();

        if (!requesterId.equals(commentAuthorId)) {
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }

        comment.updateContent(commentUpdateRequestDto.getContent());
        return new CommentUpdateResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long requesterId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_COMMENT));

        // 댓글 작성자와 게시글 작성자 ID를 가져옴
        Long commentAuthorId = comment.getUser().getUserId();
        Long postAuthorId = comment.getPost().getUser().getUserId();

        // 요청자가 댓글 작성자 또는 게시글 작성자인지 확인하는 코드
        if (!requesterId.equals(commentAuthorId) && !requesterId.equals(postAuthorId)) {
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public void deleteAllComment(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));
        List<Comment> comments = commentRepository.findAllByUser(user);
        commentRepository.deleteAll(comments);
    }
}
