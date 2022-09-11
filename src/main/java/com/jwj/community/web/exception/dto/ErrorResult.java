package com.jwj.community.web.exception.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ErrorResult {

    private final String errorCode;
    private final String errorMessage;

    @Builder
    public ErrorResult(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
