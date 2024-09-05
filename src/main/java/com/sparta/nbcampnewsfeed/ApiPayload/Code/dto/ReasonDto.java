package com.sparta.nbcampnewsfeed.ApiPayload.Code.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
public class ReasonDto {
    private String code;
    private String message;
    private HttpStatus httpStatus;
    private Boolean isSuccess;
}
