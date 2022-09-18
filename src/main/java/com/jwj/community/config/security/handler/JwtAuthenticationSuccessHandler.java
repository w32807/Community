package com.jwj.community.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwj.community.config.security.utils.JwtTokenUtil;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.refreshToken.service.RefreshTokenService;
import com.jwj.community.web.login.jwt.JwtToken;
import com.jwj.community.web.refreshToken.dto.request.RefreshTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenUtil jwtTokenUtil;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // token 생성 시 넣어주는 1번째 파라미터값이 principal이 된다.
        Member member = (Member) authentication.getPrincipal();
        JwtToken jwtToken = jwtTokenUtil.generateToken(member);

        saveRefreshToken(jwtToken, member);

        response.setStatus(OK.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());

        objectMapper.writeValue(response.getWriter(), jwtToken);
    }

    private void saveRefreshToken(JwtToken jwtToken, Member member) {
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(jwtToken.getRefreshToken())
                .build();
        // 사용자의 refresh 토큰 값을 DB에 update 해주기
        // MemberEntity에 refreshToken 값 넣어주고 서비스에 update 로직 만들기
        refreshTokenService.createRefreshToken(refreshTokenRequest.toEntity(), member);
    }
}
