package com.jwj.community.web.advice.controllerAdvice;

import com.jwj.community.web.exception.board.BoardNotFound;
import com.jwj.community.web.exception.dto.ErrorResult;
import com.jwj.community.web.exception.dto.FieldErrorResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class CommonRestControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> boardNotFoundExHandler(BoardNotFound ex){
        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(BAD_REQUEST.value())
                .errorMessage(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResult, BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<FieldErrorResult> methodArgumentNotValidExHandler(MethodArgumentNotValidException ex){
        FieldErrorResult errorResult = FieldErrorResult.builder()
                .errorCode(BAD_REQUEST.value())
                .ex(ex)
                .build();

        return new ResponseEntity<>(errorResult, BAD_REQUEST);
    }
}
