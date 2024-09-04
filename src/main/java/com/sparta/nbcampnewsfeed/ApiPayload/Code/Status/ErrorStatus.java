package com.sparta.nbcampnewsfeed.ApiPayload.Code.Status;

import com.sparta.nbcampnewsfeed.ApiPayload.Code.BaseErrorCode;
import com.sparta.nbcampnewsfeed.ApiPayload.Code.dto.ErrorReasonDto;
import jakarta.servlet.http.HttpServletResponse;
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
    _BAD_REQUEST_SAME_PASSWORD(HttpStatus.BAD_REQUEST,"400","이전 비밀번호와 같은 비밀번호로 설정 불가합니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "404", "존재하지 않는 리소스입니다."),
    _NOT_FOUND_USER(HttpStatus.NOT_FOUND, "404", "존재하지 않는 유저입니다."),
    _NOT_FOUND_POST(HttpStatus.NOT_FOUND, "404", "존재하지 않는 게시물입니다."),
    _NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "404", "존재하지 않는 댓글입니다.");



    private HttpStatus httpStatus;
    private String code;
    private String message;


    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .code(code)
                .message(message)
                .isSuccess(false)
                .build();
    }

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
