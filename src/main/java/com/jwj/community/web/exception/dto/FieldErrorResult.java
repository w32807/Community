package com.jwj.community.web.exception.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.BindException;

import java.util.ArrayList;
import java.util.List;

@Data
public class FieldErrorResult {

    private final String errorCode;
    private final List<FieldError> fieldErrors = new ArrayList<>();

    @Builder
    public FieldErrorResult(String errorCode, BindException ex){
        this.errorCode = errorCode;
        setFieldErrors(ex);
    }

    public void setFieldErrors(BindException ex){
        ex.getFieldErrors().stream()
            .map(fieldError -> FieldError.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .build())
            .forEach(fieldErrors::add);
    }

    @Data
    @Builder
    static class FieldError {
        private String field;
        private String message;
    }

}
