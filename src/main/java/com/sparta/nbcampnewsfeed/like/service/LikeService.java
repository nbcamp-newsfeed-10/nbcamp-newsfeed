package com.sparta.nbcampnewsfeed.like.service;

import com.sparta.nbcampnewsfeed.ApiPayload.Code.Status.ErrorStatus;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.comment.dto.responseDto.CommentCountResponseDto;
import com.sparta.nbcampnewsfeed.exception.ApiException;
import com.sparta.nbcampnewsfeed.friend.repository.FriendRepository;
import com.sparta.nbcampnewsfeed.like.dto.responseDto.LikeCountResponseDto;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

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

    // 좋아요 개수를 반환하는 메서드 -> 존재하지 않는 게시물의 메세지를 반환하면 되니까 post를 찾을 수 없는 항목인 NOT FOUND POST 쓰기
//    public Long getLikeCount(Long postId) {
//        Post post = postRepository.findById(postId).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_POST));
//        return likeRepository.countByPost(post);
//    }

//    public LikeCountResponseDto getLikeCount(Long postId, AuthUser authUser) {
//        Post post = postRepository.findById(postId).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_POST));
//        Long likeCount = likeRepository.countByPost(post, authUser);
//        return new LikeCountResponseDto(post, likeCount);
//    }

    public LikeCountResponseDto getLikeCount(Long postId, Long userId) {
        // 1. 현재 로그인된 사용자 정보 가져오기
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));
        // 2. 게시물 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_POST));
        User postOwner = post.getUser();
        // 3. 자신이 작성한 게시물인 경우 친구 관계 확인 생략
        if (postOwner.getUserId().equals(currentUser.getUserId())) {
            Long likeCount = likeRepository.countByPost(post);
            return new LikeCountResponseDto(post, likeCount);
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
            throw new ApiException(ErrorStatus._BAD_REQUEST_NOT_FRIEND_LIKE_GET);
        }
        // 6. 친구 관계일 경우 댓글 개수 조회
        Long likeCount = likeRepository.countByPost(post);
        return new LikeCountResponseDto(post, likeCount);
    }

}
