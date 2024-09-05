package com.sparta.nbcampnewsfeed.comment.service;

import com.sparta.nbcampnewsfeed.ApiPayload.Code.Status.ErrorStatus;
import com.sparta.nbcampnewsfeed.comment.dto.requestDto.CommentRequestDto;
import com.sparta.nbcampnewsfeed.comment.dto.requestDto.CommentUpdateRequestDto;
import com.sparta.nbcampnewsfeed.comment.dto.responseDto.*;
import com.sparta.nbcampnewsfeed.comment.entity.Comment;
import com.sparta.nbcampnewsfeed.exception.ApiException;
import com.sparta.nbcampnewsfeed.friend.entity.Friend;
import com.sparta.nbcampnewsfeed.friend.repository.FriendRepository;
import com.sparta.nbcampnewsfeed.post.entity.Post;
import com.sparta.nbcampnewsfeed.profile.entity.User;
import com.sparta.nbcampnewsfeed.comment.repository.CommentRepository;
import com.sparta.nbcampnewsfeed.post.repository.PostRepository;
import com.sparta.nbcampnewsfeed.profile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FriendRepository friendRepository;

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

    @Transactional
    public List<CommentSimpleResponseDto> getAllComment(Long postId, Long userId) {
        // 1. 현재 로그인된 사용자 정보 가져오기
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));

        // 2. 게시물 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_POST));

        User postOwner = post.getUser();

        // 3. 자신이 작성한 게시물인 경우 친구 관계 확인 생략
        if (!postOwner.getUserId().equals(currentUser.getUserId())) {
            // 4. 친구 관계 확인 (친구 상태가 true인 경우만)
            List<Long> friendIds = friendRepository.findAllByToUserAndFriendStatus(currentUser, true).stream()
                    .map(friend -> friend.getFromUser().getUserId())  // 내가 받은 요청에서 친구의 userId 가져오기
                    .collect(Collectors.toList());

            friendIds.addAll(friendRepository.findAllByFromUserAndFriendStatus(currentUser, true).stream()
                    .map(friend -> friend.getToUser().getUserId())  // 내가 보낸 요청에서 친구의 userId 가져오기
                    .collect(Collectors.toList()));

            // 본인의 userId는 제외 (필요한 경우)
            friendIds = friendIds.stream()
                    .filter(friendUserId -> !friendUserId.equals(currentUser.getUserId()))  // 본인의 userId 제외
                    .collect(Collectors.toList());

            // 게시물 작성자가 친구 목록에 있는지 확인
            if (!friendIds.contains(postOwner.getUserId())) {
                throw new IllegalArgumentException("해당 게시물의 댓글은 친구만 조회할 수 있습니다.");
            }
        }

        // 5. 친구 관계 또는 본인인 경우 댓글 조회
        List<Comment> comments = commentRepository.findByPost(post);
        return comments.stream().map(CommentSimpleResponseDto::new).toList();
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

        // 댓글 작성자와 게시글 작성자 ID를 가져온다
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

    @Transactional
    public CommentCountResponseDto getCommentCount(Long postId, Long userId) {
        // 1. 현재 로그인된 사용자 정보 가져오기
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));
        // 2. 게시물 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_POST));
        User postOwner = post.getUser();
        // 3. 자신이 작성한 게시물인 경우 친구 관계 확인 생략
        if (postOwner.getUserId().equals(currentUser.getUserId())) {
            Long commentCount = commentRepository.countByPost(post);
            return new CommentCountResponseDto(post, commentCount);
        }
        // 4. 친구 관계 확인 (친구 상태가 true인 경우만)
        List<Long> friendIds = friendRepository.findAllByToUserAndFriendStatus(currentUser, true).stream()
                .map(friend -> friend.getFromUser().getUserId())  // 내가 받은 요청에서 친구의 userId 가져오기
                .collect(Collectors.toList());
        friendIds.addAll(friendRepository.findAllByFromUserAndFriendStatus(currentUser, true).stream()
                .map(friend -> friend.getToUser().getUserId())  // 내가 보낸 요청에서 친구의 userId 가져오기
                .collect(Collectors.toList()));
        // 본인의 userId는 제외 (필요한 경우)
        friendIds = friendIds.stream()
                .filter(friendUserId -> !friendUserId.equals(currentUser.getUserId()))  // 본인의 userId 제외
                .collect(Collectors.toList());
        // 5. 게시물 작성자가 친구 목록에 있는지 확인
        if (!friendIds.contains(postOwner.getUserId())) {
            throw new IllegalArgumentException("해당 게시물에 대한 댓글 수는 친구만 조회할 수 있습니다.");
        }
        // 6. 친구 관계일 경우 댓글 개수 조회
        Long commentCount = commentRepository.countByPost(post);
        return new CommentCountResponseDto(post, commentCount);
    }
}
