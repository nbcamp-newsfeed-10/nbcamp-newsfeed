package com.sparta.nbcampnewsfeed.ApiPayload.Code.Status;

import com.sparta.nbcampnewsfeed.ApiPayload.Code.BaseCode;
import com.sparta.nbcampnewsfeed.ApiPayload.Code.dto.ReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    _OK(HttpStatus.OK, "200", "Ok");

    private HttpStatus httpStatus;
    private String code;
    private String message;

    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder()
                .code(code)
                .message(message)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDto getReasonHttpStatus() {
        return ReasonDto.builder()
                .code(code)
                .message(message)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
