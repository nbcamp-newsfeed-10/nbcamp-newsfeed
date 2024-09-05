package com.sparta.nbcampnewsfeed.post.service;

import com.sparta.nbcampnewsfeed.ApiPayload.Code.Status.ErrorStatus;
import com.sparta.nbcampnewsfeed.exception.ApiException;
import com.sparta.nbcampnewsfeed.post.dto.requestDto.PostRequestDto;
import com.sparta.nbcampnewsfeed.post.dto.responseDto.PostResponseDto;
import com.sparta.nbcampnewsfeed.post.entity.Post;
import com.sparta.nbcampnewsfeed.profile.entity.User;
import com.sparta.nbcampnewsfeed.friend.repository.FriendRepository;
import com.sparta.nbcampnewsfeed.post.repository.PostRepository;
import com.sparta.nbcampnewsfeed.profile.repository.UserRepository;
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
    private final FriendRepository friendRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, FriendRepository friendRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }

    // 게시물 작성
    @Transactional
    public PostResponseDto createPost(Long userId, PostRequestDto requestDto) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));

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
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_POST));

        // 게시물 작성자가 맞는지 확인 & 아니면 접근 금지
        if (!post.getUser().getUserId().equals(userId)) {
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
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
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_POST));

        // 게시물 작성자가 맞는지 확인 & 아니면 접근 금지
        if (!post.getUser().getUserId().equals(userId)) {
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
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
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_POST));

        // 게시물 작성자가 맞는지 확인 & 아니면 접근 금지
        if (!post.getUser().getUserId().equals(userId)) {
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }

        postRepository.delete(post);
    }

    // 친구들의 게시물 조회 (본인 게시물 제외)
    @Transactional
    public List<PostResponseDto> getNewsfeed(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));

        // 1. 친구 목록에서 친구들의 userId를 추출 (친구 상태가 true인 경우)
        List<Long> friendIds = friendRepository.findAllByToUserAndFriendStatus(user, true).stream()
                .map(friend -> friend.getFromUser().getUserId())  // 내가 받은 요청에서 친구의 userId 가져오기
                .collect(Collectors.toList());

        friendIds.addAll(friendRepository.findAllByFromUserAndFriendStatus(user, true).stream()
                .map(friend -> friend.getToUser().getUserId())  // 내가 보낸 요청에서 친구의 userId 가져오기
                .collect(Collectors.toList()));

        // 본인의 userId는 제외
        friendIds = friendIds.stream()
                .filter(friendUserId -> !friendUserId.equals(user.getUserId()))  // 본인의 userId 제외
                .collect(Collectors.toList());

        // 2. 친구들의 게시물만 조회 (페이지네이션 설정)
        Page<Post> newsfeedPage = postRepository.findAllByUserUserIdInOrderByCreatedAtDesc(
                friendIds, PageRequest.of(page, size));

        // 3. Page<Post>를 List<PostResponseDto>로 변환
        return newsfeedPage.stream()
                .map(post -> new PostResponseDto(
                        post.getPostId(),
                        post.getUser().getUserId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getCreatedAt(),
                        post.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAllPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));
        List<Post> posts = postRepository.findAllByUser(user);
        postRepository.deleteAll(posts);
    }

}