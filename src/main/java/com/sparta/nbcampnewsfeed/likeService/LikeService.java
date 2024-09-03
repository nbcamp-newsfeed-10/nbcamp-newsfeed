package com.sparta.nbcampnewsfeed.likeService;

import com.sparta.nbcampnewsfeed.entity.Like;
import com.sparta.nbcampnewsfeed.entity.Post;
import com.sparta.nbcampnewsfeed.likeDto.LikePostResponseDto;
import com.sparta.nbcampnewsfeed.likeRepository.LikeRepository;
import com.sparta.nbcampnewsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    private final LikeRepository likeRepository;
//    private final PostRepository postRepository;

    @Transactional
    public LikePostResponseDto likePost(Long post_id, String username) {
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        // 자신이 작성한 게시물에 좋아요를 누를 수 없음
        if (post.getUsername().equals(username)) {
            throw new IllegalArgumentException("You cannot like your own post");
        }

        // 이미 좋아요를 눌렀는지 확인
        if (likeRepository.findByPostIdAndUsername(post_id, username).isPresent()) {
            throw new IllegalArgumentException("You have already liked this post");
        }

        // 좋아요 추가
        post.incrementLike();
        likeRepository.save(Like.builder().post(post).username(username).build());

        return new LikePostResponseDto(post.getId(), post.getLikeCount());
    }

    @Transactional
    public LikePostResponseDto unlikePost(Long post_id, String username) {
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        // 좋아요 기록이 있는지 확인
        Like like = likeRepository.findByPostIdAndUsername(post_id, username)
                .orElseThrow(() -> new IllegalArgumentException("You haven't liked this post yet"));

        // 좋아요 취소
        post.decrementLike();
        likeRepository.delete(like);

        return new LikePostResponseDto(post.getId(), post.getLikeCount());
    }

}
