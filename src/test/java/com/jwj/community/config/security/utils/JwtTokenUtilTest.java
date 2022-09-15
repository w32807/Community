package com.jwj.community.config.security.utils;

import com.jwj.community.domain.common.enums.Roles;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.web.login.jwt.JwtToken;
import com.jwj.community.web.login.request.MemberSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.jwj.community.domain.common.enums.Roles.ROLE_ADMIN;
import static com.jwj.community.domain.common.enums.Roles.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    Member member;

    @BeforeEach
    void setup(){
        member = MemberSaveRequest.builder()
                .email("admin@google.com")
                .password("1234")
                .nickname("닉네임")
                .build()
                .toEntity();
    }

    @Test
    @DisplayName("회원정보가 유효할 때 access 토큰얻기")
    void test1() throws Exception{
        JwtToken token = jwtTokenUtil.generateToken(member);

        assertThat(token.getAccessToken()).isNotNull();
    }

    @Test
    @DisplayName("회원정보가 유효할 때 refresh 토큰얻기")
    void test2() throws Exception{
        JwtToken token = jwtTokenUtil.generateToken(member);

        assertThat(token.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("회원정보가 유효하지 않을 때 access 토큰은 null")
    void test3() throws Exception{
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder().build();

        JwtToken token = jwtTokenUtil.generateToken(memberSaveRequest.toEntity());
        assertThat(token.getAccessToken()).isNull();
    }

    @Test
    @DisplayName("회원정보가 유효하지 않을 때 refresh 토큰은 null")
    void test4() throws Exception{
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder().build();

        JwtToken token = jwtTokenUtil.generateToken(memberSaveRequest.toEntity());
        assertThat(token.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("회원이 null일 때 access 토큰은 null")
    void test8() throws Exception{
        JwtToken token = jwtTokenUtil.generateToken(null);
        assertThat(token.getAccessToken()).isNull();
    }

    @Test
    @DisplayName("회원이 null일 때 refresh 토큰은 null")
    void test9() throws Exception{
        JwtToken token = jwtTokenUtil.generateToken(null);
        assertThat(token.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("토큰에서 username 조회")
    void test5() throws Exception{
        JwtToken token = jwtTokenUtil.generateToken(member);
        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(token.getAccessToken());

        assertThat(usernameFromToken).isEqualTo("admin@google.com");
    }

    @Test
    @DisplayName("토큰에서 권한조회 조회")
    void test6() throws Exception{
        member.addRole(ROLE_ADMIN);
        member.addRole(ROLE_MEMBER);

        JwtToken token = jwtTokenUtil.generateToken(member);
        Set<Roles> rolesFromToken = jwtTokenUtil.getRolesFromToken(token.getAccessToken());
        assertThat(rolesFromToken)
                .hasSize(2)
                .contains(ROLE_ADMIN, ROLE_MEMBER);
    }

    @Test
    @DisplayName("토큰에서 권한이 없는데 권한 조회")
    void test7() throws Exception{
        JwtToken token = jwtTokenUtil.generateToken(member);
        Set<Roles> rolesFromToken = jwtTokenUtil.getRolesFromToken(token.getAccessToken());
        assertThat(rolesFromToken).isEmpty();
    }

}