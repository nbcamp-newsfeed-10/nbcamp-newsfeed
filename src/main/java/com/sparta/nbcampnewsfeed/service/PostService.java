package com.sparta.nbcampnewsfeed.service;

import com.sparta.nbcampnewsfeed.dto.PostRequestDto;
import com.sparta.nbcampnewsfeed.dto.PostResponseDto;
import com.sparta.nbcampnewsfeed.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.entity.Friend;
import com.sparta.nbcampnewsfeed.entity.Post;
import com.sparta.nbcampnewsfeed.entity.User;
import com.sparta.nbcampnewsfeed.repository.FriendRepository;
import com.sparta.nbcampnewsfeed.repository.PostRepository;
import com.sparta.nbcampnewsfeed.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final FriendRepository friendRepository;
    private final FriendService friendService;

    public PostService(PostRepository postRepository, UserRepository userRepository, UserService userService, FriendRepository friendRepository, FriendService friendService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.friendRepository = friendRepository;
        this.friendService = friendService;
    }

    // 게시물 작성
    @Transactional
    public PostResponseDto createPost(Long userId, PostRequestDto requestDto) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        // 게시물 생성
        Post post = new Post(user, requestDto.getTitle(), requestDto.getContent());

        // 게시물 저장
        postRepository.save(post);

        // post 엔티티를 PostResponseDto 로 변환하여 반환
        return new PostResponseDto(
                post.getPostId(),
                post.getUser().getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    // 게시물 수정
    @Transactional
    public PostResponseDto updatePost(Long userId, Long postId, PostRequestDto requestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        // 게시물 작성자가 맞는지 확인 & 아니면 접근 금지
        if (!post.getUser().getUserId().equals(userId)) {
            try {
                throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        post.update(requestDto.getTitle(), requestDto.getContent());
        return new PostResponseDto(
                post.getPostId(),
                post.getUser().getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    // 게시물 조회
    @Transactional
    public PostResponseDto getPost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        // 게시물 작성자가 맞는지 확인 & 아니면 접근 금지
        if (!post.getUser().getUserId().equals(userId)) {
            try {
                throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        return new PostResponseDto(
                post.getPostId(),
                post.getUser().getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    // 게시물 삭제
    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        // 게시물 작성자가 맞는지 확인 & 아니면 접근 금지
        if (!post.getUser().getUserId().equals(userId)) {
            try {
                throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        postRepository.delete(post);
    }

    // 친구들의 게시물 조회 (페이지네이션 조회)
    @Transactional
    public Page<PostResponseDto> getNewsfeed(Long userId, int page, int size) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        // 사용자의 친구 목록 조회
        List<Friend> friends = friendRepository.findAllByToUserAndFriendStatus(user, true);

        // 친구들의 Id 추출
        List<Long> friendIds = friends.stream()
                .map(friend -> friend.getFromUser().getUserId())
                .collect(Collectors.toList());

        // 페이지네이션 설정
        PageRequest pageRequest = PageRequest.of(page, size);

        // 친구들의 게시물만 조회 (친구들의 Id를 사용)
        return postRepository.findAllByUserUserIdInOrderByCreatedAtDesc(friendIds, pageRequest)
                .map(post -> new PostResponseDto(
                        post.getPostId(),
                        post.getUser().getUserId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getCreatedAt(),
                        post.getUpdatedAt()
                ));
    }
}