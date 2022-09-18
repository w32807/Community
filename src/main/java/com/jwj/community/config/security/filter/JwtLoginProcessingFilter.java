package com.jwj.community.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwj.community.config.security.token.JwtAuthenticationToken;
import com.jwj.community.web.login.request.MemberSaveRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.ObjectUtils.isEmpty;

public class JwtLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public JwtLoginProcessingFilter(){
        // 요청 url이 "api/login"과 동일하면 필터 작동
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 입력받은 로그인 정보 확인
        MemberSaveRequest memberSaveRequest = objectMapper.readValue(request.getReader(), MemberSaveRequest.class);

        if(isEmpty(memberSaveRequest.getEmail())) throw new UsernameNotFoundException("아이디를 확인 해 주세요");
        if(isEmpty(memberSaveRequest.getPassword())) throw new BadCredentialsException("비밀번호를 확인 해 주세요");

        // 인증을 요청할 때 사용되는 토큰을 생성하여 AuthenticationManager로 인증 위임
        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(memberSaveRequest.getEmail(), memberSaveRequest.getPassword()));
    }

}
