package com.jwj.community.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwj.community.config.security.token.AjaxAuthenticationToken;
import com.jwj.community.web.login.request.MemberSaveRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.ObjectUtils.isEmpty;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public AjaxLoginProcessingFilter(){
        // 요청 url이 "api/login"과 동일하면 필터 작동
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        // Ajax 요청인지 확인
        if(!isAjax(request)){
            throw new IllegalStateException("Ajax 요청이 아닙니다.");
        }

        // 입력받은 로그인 정보가 유효한지 확인
        MemberSaveRequest memberSaveRequest = objectMapper.readValue(request.getReader(), MemberSaveRequest.class);

        if(isEmpty(memberSaveRequest.getEmail()) || isEmpty(memberSaveRequest.getPassword())){
            throw new IllegalArgumentException("이메일 혹은 비밀번호를 확인 해 주세요");
        }

        // 인증을 요청할 때 사용되는 토큰을 생성하여 AuthenticationManager로 인증 위임
        return getAuthenticationManager().authenticate(new AjaxAuthenticationToken(memberSaveRequest.getEmail(), memberSaveRequest.getPassword()));
    }

    private boolean isAjax(HttpServletRequest request) {
        // request header에 XMLHttpRequest 값이 있으면 Ajax 요청이라고 판단.
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
