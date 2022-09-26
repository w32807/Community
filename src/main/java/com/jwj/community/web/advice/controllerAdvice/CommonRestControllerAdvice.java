package com.jwj.community.web.advice.controllerAdvice;

import com.jwj.community.web.exception.dto.ErrorResult;
import com.jwj.community.web.exception.dto.FieldErrorResult;
import com.jwj.community.web.exception.exceptions.common.CommunityException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.lang.Integer.parseInt;
import static java.util.Locale.getDefault;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
@RequiredArgsConstructor
public class CommonRestControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler
    public ResponseEntity<ErrorResult> communityExHandler(CommunityException ex){
        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(ex.getStatusCode())
                .errorMessage(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResult, HttpStatus.valueOf(parseInt(ex.getStatusCode())));
    }

    @ExceptionHandler
    public ResponseEntity<FieldErrorResult> methodArgumentNotValidExHandler(MethodArgumentNotValidException ex){
        FieldErrorResult errorResult = FieldErrorResult.builder()
                .errorCode(String.valueOf(BAD_REQUEST.value()))
                .ex(ex)
                .build();

        return new ResponseEntity<>(errorResult, BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> expiredJwtExceptionExHandler(ExpiredJwtException ex){
        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(String.valueOf(UNAUTHORIZED.value()))
                .errorMessage(messageSource.getMessage("error.expiredJwtToken", null, getDefault()))
                .build();

        return new ResponseEntity<>(errorResult, UNAUTHORIZED);
    }
}
