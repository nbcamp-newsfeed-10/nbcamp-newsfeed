package com.sparta.nbcampnewsfeed.service;

import com.sparta.nbcampnewsfeed.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.entity.Like;
import com.sparta.nbcampnewsfeed.entity.Post;
import com.sparta.nbcampnewsfeed.entity.User;
import com.sparta.nbcampnewsfeed.dto.responseDto.LikePostResponseDto;
import com.sparta.nbcampnewsfeed.repository.LikeRepository;
import com.sparta.nbcampnewsfeed.repository.PostRepository;
import com.sparta.nbcampnewsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public LikePostResponseDto likePost(Long postId, AuthUser authUser) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        User user = userRepository.findById(authUser.getId()).orElseThrow(()-> new IllegalArgumentException("Invalid user"));

        // 자신이 작성한 게시물에 좋아요를 누를 수 없음
        if (post.getUser().getUserId().equals(user.getUserId())) { //로그인한 유저의 아이디와 게시물 작성한 유저의 아이디를 비교하는 것
            throw new IllegalArgumentException("You cannot like your own post");
        }

        // 이미 좋아요를 눌렀는지 확인 //데이터베이스에 포스트아이디랑 회원아이디가 있는지 확인해서 있다면 좋아요를 눌렀다는 증거!
        if (likeRepository.findByPostAndUser(post, user).isPresent()) {
            throw new IllegalArgumentException("You have already liked this post");
        }

        // 좋아요 추가
        Like like = new Like(user, post);
        Like saveLike = likeRepository.save(like);

        return new LikePostResponseDto(saveLike);
    }

    @Transactional
    public void unlikePost(Long postId, AuthUser authUser) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        User user = userRepository.findById(authUser.getId()).orElseThrow(()-> new IllegalArgumentException("Invalid user"));

        // 좋아요 기록이 있는지 확인
        Like like = likeRepository.findByPostAndUser(post, user).orElseThrow(() -> new IllegalArgumentException("You haven't liked this post yet"));

        // 좋아요 취소
        likeRepository.delete(like);
    }

}
