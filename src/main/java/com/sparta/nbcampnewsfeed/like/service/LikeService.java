package com.sparta.nbcampnewsfeed.like.service;

import com.sparta.nbcampnewsfeed.ApiPayload.Code.Status.ErrorStatus;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.exception.ApiException;
import com.sparta.nbcampnewsfeed.like.entity.Like;
import com.sparta.nbcampnewsfeed.post.entity.Post;
import com.sparta.nbcampnewsfeed.profile.entity.User;
import com.sparta.nbcampnewsfeed.like.dto.responseDto.LikePostResponseDto;
import com.sparta.nbcampnewsfeed.like.repository.LikeRepository;
import com.sparta.nbcampnewsfeed.post.repository.PostRepository;
import com.sparta.nbcampnewsfeed.profile.repository.UserRepository;
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
        Post post = postRepository.findById(postId).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_POST));
        User user = userRepository.findById(authUser.getId()).orElseThrow(()-> new ApiException(ErrorStatus._NOT_FOUND_USER));

        // 자신이 작성한 게시물에 좋아요를 누를 수 없음
        if (post.getUser().getUserId().equals(user.getUserId())) { //로그인한 유저의 아이디와 게시물 작성한 유저의 아이디를 비교하는 것
            throw new ApiException(ErrorStatus._BAD_REQUEST_SELF_LIKE);
        }

        // 이미 좋아요를 눌렀는지 확인 //데이터베이스에 포스트아이디랑 회원아이디가 있는지 확인해서 있다면 좋아요를 눌렀다는 증거!
        if (likeRepository.findByPostAndUser(post, user).isPresent()) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_DOUBLE_LIKE);
        }

        // 좋아요 추가
        Like like = new Like(user, post);
        Like saveLike = likeRepository.save(like);

        return new LikePostResponseDto(saveLike);
    }

    @Transactional
    public void unlikePost(Long postId, AuthUser authUser) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_POST));
        User user = userRepository.findById(authUser.getId()).orElseThrow(()-> new ApiException(ErrorStatus._NOT_FOUND_USER));

        // 좋아요 기록이 있는지 확인
        Like like = likeRepository.findByPostAndUser(post, user).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_LIKE));

        // 좋아요 취소
        likeRepository.delete(like);
    }

}
