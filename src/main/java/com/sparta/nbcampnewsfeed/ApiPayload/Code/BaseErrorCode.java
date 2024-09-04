package com.sparta.nbcampnewsfeed.ApiPayload.Code;

import com.sparta.nbcampnewsfeed.ApiPayload.Code.dto.ErrorReasonDto;

public interface BaseErrorCode {
    public ErrorReasonDto getReason();

    public ErrorReasonDto getReasonHttpStatus();
}
