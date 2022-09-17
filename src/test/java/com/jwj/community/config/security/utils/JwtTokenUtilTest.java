package com.jwj.community.config.security.utils;

import com.jwj.community.domain.common.enums.Roles;
import com.jwj.community.web.code.jwt.JwtTokenFactory;
import com.jwj.community.web.login.jwt.JwtToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.jwj.community.domain.common.enums.Roles.ROLE_ADMIN;
import static com.jwj.community.domain.common.enums.Roles.ROLE_MEMBER;
import static com.jwj.community.web.code.jwt.JwtTokenFactory.getJwtToken;
import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenUtilTest {

    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @Test
    @DisplayName("회원정보가 유효할 때 access 토큰얻기")
    void test1(){
        JwtToken token = getJwtToken();
        System.out.println("token = " + token);
        assertThat(token.getAccessToken()).isNotNull();
    }

    @Test
    @DisplayName("회원정보가 유효할 때 refresh 토큰얻기")
    void test2(){
        JwtToken token = getJwtToken();
        assertThat(token.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("회원정보가 유효하지 않을 때 access 토큰은 null")
    void test3(){
        JwtToken token = JwtTokenFactory.getNoEmailJwtToken();
        assertThat(token.getAccessToken()).isNull();
    }

    @Test
    @DisplayName("회원정보가 유효하지 않을 때 refresh 토큰은 null")
    void test4(){
        JwtToken token = JwtTokenFactory.getNoEmailJwtToken();
        assertThat(token.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("회원이 null일 때 access 토큰은 null")
    void test8(){
        JwtToken token = JwtTokenFactory.getNoEmailJwtToken();
        assertThat(token.getAccessToken()).isNull();
    }

    @Test
    @DisplayName("회원이 null일 때 refresh 토큰은 null")
    void test9(){
        JwtToken token = JwtTokenFactory.getNoMemberJwtToken();
        assertThat(token.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("토큰에서 username 조회")
    void test5(){
        JwtToken token = getJwtToken();
        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(token.getAccessToken());

        assertThat(usernameFromToken).isEqualTo("admin@google.com");
    }

    @Test
    @DisplayName("토큰에서 권한조회 조회")
    void test6(){
        JwtToken token = getJwtToken();
        Set<Roles> rolesFromToken = jwtTokenUtil.getRolesFromToken(token.getAccessToken());
        assertThat(rolesFromToken)
                .hasSize(2)
                .contains(ROLE_ADMIN, ROLE_MEMBER);
    }

    @Test
    @DisplayName("토큰에서 권한이 없는데 권한 조회")
    void test7(){
        JwtToken token = JwtTokenFactory.getNoAuthJwtToken();
        Set<Roles> rolesFromToken = jwtTokenUtil.getRolesFromToken(token.getAccessToken());
        assertThat(rolesFromToken).isEmpty();
    }
}