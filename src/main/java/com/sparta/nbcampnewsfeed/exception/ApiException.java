package com.sparta.nbcampnewsfeed.exception;

import com.sparta.nbcampnewsfeed.ApiPayload.Code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {

    private final BaseErrorCode errorCode;

}
