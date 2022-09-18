package com.jwj.community.config.security.utils;

import com.jwj.community.domain.common.enums.Roles;
import com.jwj.community.web.code.jwt.JwtTokenFactory;
import com.jwj.community.web.login.jwt.JwtToken;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static com.jwj.community.domain.common.enums.Roles.ROLE_ADMIN;
import static com.jwj.community.domain.common.enums.Roles.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtTokenFactory jwtTokenFactory;


    @Test
    @DisplayName("회원정보가 유효할 때 access 토큰얻기")
    void test1(){
       JwtToken token = jwtTokenFactory.getJwtToken();
        assertThat(token.getAccessToken()).isNotNull();
    }

    @Test
    @DisplayName("회원정보가 유효할 때 refresh 토큰얻기")
    void test2(){
        JwtToken token = jwtTokenFactory.getJwtToken();
        assertThat(token.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("회원정보가 유효하지 않을 때 access 토큰은 null")
    void test3(){
        JwtToken token = jwtTokenFactory.getNoEmailJwtToken();
        assertThat(token.getAccessToken()).isNull();
    }

    @Test
    @DisplayName("회원정보가 유효하지 않을 때 refresh 토큰은 null")
    void test4(){
        JwtToken token = jwtTokenFactory.getNoEmailJwtToken();
        assertThat(token.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("회원이 null일 때 access 토큰은 null")
    void test8(){
        JwtToken token = jwtTokenFactory.getNoEmailJwtToken();
        assertThat(token.getAccessToken()).isNull();
    }

    @Test
    @DisplayName("회원이 null일 때 refresh 토큰은 null")
    void test9(){
        JwtToken token = jwtTokenFactory.getNoMemberJwtToken();
        assertThat(token.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("토큰에서 username 조회")
    void test5(){
        JwtToken token = jwtTokenFactory.getJwtToken();
        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(token.getAccessToken());

        assertThat(usernameFromToken).isEqualTo("admin@google.com");
    }

    @Test
    @DisplayName("토큰에서 권한조회 조회")
    void test6(){
        JwtToken token = jwtTokenFactory.getJwtToken();
        Set<Roles> rolesFromToken = jwtTokenUtil.getRolesFromToken(token.getAccessToken());
        assertThat(rolesFromToken)
                .hasSize(2)
                .contains(ROLE_ADMIN, ROLE_MEMBER);
    }

    @Test
    @DisplayName("토큰에서 권한이 없는데 권한 조회")
    void test7(){
        JwtToken token = jwtTokenFactory.getNoAuthJwtToken();
        Set<Roles> rolesFromToken = jwtTokenUtil.getRolesFromToken(token.getAccessToken());
        assertThat(rolesFromToken).isEmpty();
    }

    @Test
    @DisplayName("토큰이 만료 되었을 때")
    void test10(){
        assertThatThrownBy(() -> {
            JwtToken token = jwtTokenFactory.getExpiredJwtToken();
            jwtTokenUtil.isExpiredToken(token.getAccessToken());
        }).isInstanceOf(ExpiredJwtException.class);
    }
}