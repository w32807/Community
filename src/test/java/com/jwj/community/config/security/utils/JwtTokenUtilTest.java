package com.jwj.community.config.security.utils;

import com.jwj.community.web.login.request.JwtRequest;
import com.jwj.community.web.login.request.MemberSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    MemberSaveRequest request;

    @BeforeEach
    void setup(){
        request = MemberSaveRequest.builder()
                .email("admin@google.com")
                .password("1234")
                .nickname("닉네임")
                .build();
    }

    @Test
    @DisplayName("토큰 생성 성공")
    void test1() throws Exception{
        // given
        // when
        JwtRequest jwtRequest = jwtTokenUtil.generateToken(request.toEntity());

        // then
        assertThat(jwtRequest.getToken()).isNotNull();
    }

    @Test
    @DisplayName("토큰으로부터 회원 이메일 얻기")
    void test2() throws Exception{
        // given
        // when
        JwtRequest jwtRequest = jwtTokenUtil.generateToken(request.toEntity());
        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(jwtRequest.getToken());

        // then
        assertThat(usernameFromToken).isEqualTo("admin@google.com");
    }

    @Test
    @DisplayName("토큰으로부터 만료시간 얻기")
    void test3() throws Exception{
        // given
        // when
        JwtRequest jwtRequest = jwtTokenUtil.generateToken(request.toEntity());
        Date expirationDateFromToken = jwtTokenUtil.getExpirationDateFromToken(jwtRequest.getToken());

        System.out.println("expirationDateFromToken = " + expirationDateFromToken);
        // then
        //assertThat(usernameFromToken).isEqualTo("admin@google.com");
    }

}