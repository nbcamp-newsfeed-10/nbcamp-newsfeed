package com.sparta.nbcampnewsfeed.profile.service;

import com.sparta.nbcampnewsfeed.ApiPayload.Code.Status.ErrorStatus;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.SigninRequest;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.SignupRequestDto;
import com.sparta.nbcampnewsfeed.config.JwtUtil;
import com.sparta.nbcampnewsfeed.config.PasswordEncoder;
import com.sparta.nbcampnewsfeed.auth.dto.responseDto.SignupResponseDto;
import com.sparta.nbcampnewsfeed.exception.ApiException;
import com.sparta.nbcampnewsfeed.profile.dto.requestDto.UserProfileUpdateRequestDto;
import com.sparta.nbcampnewsfeed.profile.dto.requestDto.WithdrawRequestDto;
import com.sparta.nbcampnewsfeed.profile.dto.responseDto.UserProfileMeResponseDto;
import com.sparta.nbcampnewsfeed.profile.dto.responseDto.UserProfileResponseDto;
import com.sparta.nbcampnewsfeed.profile.dto.responseDto.UserProfileUpdateResponseDto;
import com.sparta.nbcampnewsfeed.profile.entity.User;
import com.sparta.nbcampnewsfeed.post.service.PostService;
import com.sparta.nbcampnewsfeed.profile.repository.UserRepository;
import com.sparta.nbcampnewsfeed.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PostService postService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CommentService commentService;

    @Transactional
    public SignupResponseDto save(SignupRequestDto signUpRequestDto) {

        // 중복된 email 가입 시도시
        userRepository.findByEmail(signUpRequestDto.getEmail())
                .ifPresent(u -> {throw new ApiException(ErrorStatus._BAD_REQUEST_EMAIL);});

        User user = new User(signUpRequestDto.getUsername(), signUpRequestDto.getEmail(), signUpRequestDto.getBio(),
                passwordEncoder.encode(signUpRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        String token = jwtUtil.createToken(savedUser.getUserId(), savedUser.getEmail());
        return SignupResponseDto.of(savedUser, token);
    }

    public String signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));

        // 탈퇴한 회원
        if (!user.isActive()) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_USER);
        }

        // 비밀번호 불일치
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_PASSWORD);
        }

        return jwtUtil.createToken(user.getUserId(), user.getEmail());
    }

    public UserProfileMeResponseDto getUserProfileForMe(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));
        return new UserProfileMeResponseDto(user);
    }

    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));
        return new UserProfileResponseDto(user);
    }

    @Transactional
    public UserProfileUpdateResponseDto updateUserProfile(Long userId, UserProfileUpdateRequestDto updateRequest,
                                                          AuthUser authUser) {
        // 로그인 회원과 수정하려는 프로필 회원이 다를 때
        if (!authUser.getId().equals(userId)) {
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));

        // 비밀번호 변경 처리
        if (updateRequest.getCurrentPassword() != null && updateRequest.getNewPassword() != null) {
            if (!passwordEncoder.matches(updateRequest.getCurrentPassword(), user.getPassword())) {
                throw new ApiException(ErrorStatus._BAD_REQUEST_PASSWORD);
            }

            if (passwordEncoder.matches(updateRequest.getNewPassword(), user.getPassword())) {
                throw new ApiException(ErrorStatus._BAD_REQUEST_SAME_PASSWORD);
            }

            user.changePassword(passwordEncoder.encode(updateRequest.getNewPassword()));
        }

        // 프로필 정보 수정 처리
        user.updateProfile(updateRequest.getUsername(), updateRequest.getBio());
        userRepository.save(user);

        return new UserProfileUpdateResponseDto(user);
    }

    @Transactional
    public void withdraw(WithdrawRequestDto requestDto, AuthUser authUser) {
        // 로그인 회원과 탈퇴를 시도하는 회원이 다를 때
        if (!authUser.getEmail().equals(requestDto.getEmail())) {
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));

        // 이미 탈퇴한 회원
        if (!user.isActive()) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_USER);
        }

        // 비밀번호 불일치
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_PASSWORD);
        }

        // 작성한 게시물 전체 삭제
        postService.deleteAllPosts(authUser.getId());
        // 작성한 댓글 전체 삭제
        commentService.deleteAllComment(authUser.getId());

        user.withdraw();
    }
}
