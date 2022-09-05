package com.jwj.community.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwj.community.web.exception.dto.ErrorResult;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
        throws IOException, ServletException {

        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(UNAUTHORIZED.value())
                .errorMessage(getErrorMessage(exception))
                .build();

        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());

        objectMapper.writeValue(response.getWriter(), errorResult);
    }

    private String getErrorMessage(AuthenticationException exception) {
        String errorMessage = "이메일 혹은 비밀번호를 확인 해 주세요.";

        if(exception instanceof UsernameNotFoundException){
            errorMessage = "이메일을 확인 해 주세요.";
        }

        if(exception instanceof BadCredentialsException){
            errorMessage = "비밀번호를 확인 해 주세요.";
        }

        if(exception instanceof InsufficientAuthenticationException){
            errorMessage = "추가 인증 실패.";
        }

        if(exception instanceof AuthenticationServiceException){
            errorMessage = exception.getMessage();
        }

        return errorMessage;
    }
}
