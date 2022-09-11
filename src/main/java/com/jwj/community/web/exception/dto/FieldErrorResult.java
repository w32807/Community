package com.jwj.community.web.exception.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

@Data
public class FieldErrorResult {

    private final int errorCode;
    private final List<FieldError> fieldErrors = new ArrayList<>();

    @Builder
    public FieldErrorResult(int errorCode, MethodArgumentNotValidException ex){
        this.errorCode = errorCode;
        setFieldErrors(ex);
    }

    public void setFieldErrors(MethodArgumentNotValidException ex){
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
