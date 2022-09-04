package com.jwj.community.config.security.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwj.community.web.exception.dto.ErrorResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AjaxLoginAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // 익명사용자가 인가가 필요한 자원에 접근하였을 때 처리
        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(UNAUTHORIZED.value())
                .errorMessage("인증되지 않은 사용자입니다.")
                .build();

        response.setStatus(UNAUTHORIZED.value());
        response.setCharacterEncoding(UTF_8.name());
        response.setContentType(APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getWriter(), errorResult);
    }
}
