package com.sparta.nbcampnewsfeed.ApiPayload.Code.Status;

import com.sparta.nbcampnewsfeed.ApiPayload.Code.BaseErrorCode;
import com.sparta.nbcampnewsfeed.ApiPayload.Code.dto.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Interval server error"),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"400","잘못된 요청입니다."),
    _BAD_REQUEST_EMAIL(HttpStatus.BAD_REQUEST,"400","중복된 이메일입니다."),
    _BAD_REQUEST_USER(HttpStatus.BAD_REQUEST,"400","탈퇴한 회원입니다."),
    _BAD_REQUEST_PASSWORD(HttpStatus.BAD_REQUEST,"400","잘못된 비밀번호입니다."),
    _BAD_REQUEST_SELF_LIKE(HttpStatus.BAD_REQUEST,"400","자신의 게시물에 좋아요를 남길 수 없습니다."),
    _BAD_REQUEST_DOUBLE_LIKE(HttpStatus.BAD_REQUEST,"400","같은 게시물에는 좋아요를 한 번만 남길 수 있습니다."),
    _BAD_REQUEST_SAME_PASSWORD(HttpStatus.BAD_REQUEST,"400","이전 비밀번호와 같은 비밀번호로 설정 불가합니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "404", "존재하지 않는 리소스입니다."),
    _NOT_FOUND_USER(HttpStatus.NOT_FOUND, "404", "존재하지 않는 유저입니다."),
    _NOT_FOUND_POST(HttpStatus.NOT_FOUND, "404", "존재하지 않는 게시물입니다."),
    _NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "404", "존재하지 않는 댓글입니다."),
    _NOT_FOUND_LIKE(HttpStatus.NOT_FOUND, "404", "존재하지 않는 좋아요입니다."),
    // friend 예외처리
    _BAD_REQUEST_SELF_FRIEND(HttpStatus.BAD_REQUEST, "400","자기 자신에게 친구 신청을 할 수 없습니다."),
    _BAD_REQUEST_FROM_USER(HttpStatus.BAD_REQUEST, "400", "이미 친구이거나 친구 신청을 한 회원입니다."),
    _BAD_REQUEST_TO_USER(HttpStatus.BAD_REQUEST, "400", "이미 해당 회원으로부터 친구 신청을 받은 상태입니다."),
    _NOT_FOUND_FRIEND(HttpStatus.NOT_FOUND, "404", "해당 회원에게 받은 친구 추가 요청이 없습니다."),
    _BAD_REQUEST_ALREADY_FRIEND(HttpStatus.BAD_REQUEST,"400", "이미 친구인 회원입니다."),
    _BAD_REQUEST_NOT_FRIEND(HttpStatus.BAD_REQUEST, "400", "친구가 아닌 회원입니다."),
    // token 예외 처리
    _BAD_REQUEST_UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST,"400","지원되지 않는 JWT 토큰입니다."),
    _BAD_REQUEST_ILLEGAL_TOKEN(HttpStatus.BAD_REQUEST,"400","잘못된 JWT 토큰입니다."),
    _UNAUTHORIZED_INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"401","유효하지 않는 JWT 서명입니다."),
    _UNAUTHORIZED_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"401","만료된 JWT 토큰입니다."),
    _UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED,"401","JWT 토큰 검증 중 오류가 발생했습니다."),
    _NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "404", "JWT 토큰이 필요합니다."),
    // comment 추가 예외처리
    _BAD_REQUEST_NOT_FRIEND_COMMENT_GET(HttpStatus.BAD_REQUEST, "400", "해당 게시물의 댓글은 친구만 조회할 수 있습니다."),
    _BAD_REQUEST_NOT_FRIEND_COMMENT_COUNT(HttpStatus.BAD_REQUEST, "400", "해당 게시물에 대한 댓글 수는 친구만 조회할 수 있습니다."),
    // like 추가 예외처리
    _BAD_REQUEST_NOT_FRIEND_LIKE_GET(HttpStatus.BAD_REQUEST,"400", "해당 게시물에 대한 좋아요 수는 친구만 조회할 수 있습니다.");

    private HttpStatus httpStatus;
    private String code;
    private String message;

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .code(code)
                .message(message)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
