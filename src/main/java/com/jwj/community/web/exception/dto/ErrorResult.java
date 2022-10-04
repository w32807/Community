package com.jwj.community.web.exception.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ErrorResult {

    private final String errorCode;
    private final String errorMessage;
    private final Exception exception;
    private final String exceptionName;

    @Builder
    public ErrorResult(String errorCode, String errorMessage, Exception exception){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.exception = exception;
        this.exceptionName = exception.getClass().getSimpleName();
    }

}
