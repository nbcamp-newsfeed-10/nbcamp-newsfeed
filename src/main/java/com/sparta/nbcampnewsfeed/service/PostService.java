package com.sparta.nbcampnewsfeed.service;

import com.sparta.nbcampnewsfeed.dto.PostRequestDto;
import com.sparta.nbcampnewsfeed.dto.PostResponseDto;
import com.sparta.nbcampnewsfeed.entity.Post;
import com.sparta.nbcampnewsfeed.entity.User;
import com.sparta.nbcampnewsfeed.repository.PostRepository;
import com.sparta.nbcampnewsfeed.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // create Post
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
}
