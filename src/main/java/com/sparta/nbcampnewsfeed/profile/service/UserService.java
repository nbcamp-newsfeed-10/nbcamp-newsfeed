package com.sparta.nbcampnewsfeed.profile.service;

import com.sparta.nbcampnewsfeed.auth.dto.requestDto.AuthUser;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.SigninRequest;
import com.sparta.nbcampnewsfeed.auth.dto.requestDto.SignupRequestDto;
import com.sparta.nbcampnewsfeed.config.JwtUtil;
import com.sparta.nbcampnewsfeed.config.PasswordEncoder;
import com.sparta.nbcampnewsfeed.auth.dto.responseDto.SignupResponseDto;
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
                .ifPresent(u -> {throw new IllegalArgumentException();});

        User user = new User(signUpRequestDto.getUsername(), signUpRequestDto.getEmail(), signUpRequestDto.getBio(),
                passwordEncoder.encode(signUpRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        String token = jwtUtil.createToken(savedUser.getUserId(), savedUser.getEmail());
        return SignupResponseDto.of(savedUser, token);
    }

    public String signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 탈퇴한 회원
        if (!user.isActive()) {
            throw new IllegalArgumentException("withdraw user");
        }

        // 비밀번호 불일치
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("wrong password");
        }

        return jwtUtil.createToken(user.getUserId(), user.getEmail());
    }

    public UserProfileMeResponseDto getUserProfileForMe(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("The user is not me"));
        return new UserProfileMeResponseDto(user);
    }

    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserProfileResponseDto(user);
    }

    @Transactional
    public UserProfileUpdateResponseDto updateUserProfile(Long userId, UserProfileUpdateRequestDto updateRequest,
                                                          AuthUser authUser) {
        // 로그인 회원과 수정하려는 프로필 회원이 다를 때
        if (!authUser.getId().equals(userId)) {
            throw new IllegalArgumentException("can not update other profile");
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        // 비밀번호 변경 처리
        if (updateRequest.getCurrentPassword() != null && updateRequest.getNewPassword() != null) {
            if (!passwordEncoder.matches(updateRequest.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }

            if (passwordEncoder.matches(updateRequest.getNewPassword(), user.getPassword())) {
                throw new IllegalArgumentException("New password cannot be the same as the current password");
            }

            if (!isValidPassword(updateRequest.getNewPassword())) {
                throw new IllegalArgumentException("New password does not meet the required format");
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
            throw new IllegalArgumentException("자신의 계정만 삭제 가능합니다.");
        }

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 이미 탈퇴한 회원
        if (!user.isActive()) {
            throw new IllegalArgumentException("withdraw user");
        }

        // 비밀번호 불일치
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }

        // 작성한 게시물 전체 삭제
        postService.deleteAllPosts(authUser.getId());
        // 작성한 댓글 전체 삭제
        commentService.deleteAllComment(authUser.getId());

        user.withdraw();
    }

    private boolean isValidPassword(String password) {
        // 최소 8자, 대소문자 포함 영문, 숫자, 특수문자 포함 형식 검증
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }
}
