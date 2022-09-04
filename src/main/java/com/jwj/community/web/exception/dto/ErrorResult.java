package com.jwj.community.web.exception.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ErrorResult {

    private final int errorCode;
    private final String errorMessage;

    @Builder
    public ErrorResult(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
