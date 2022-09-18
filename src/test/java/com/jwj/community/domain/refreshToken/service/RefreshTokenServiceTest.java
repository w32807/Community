package com.jwj.community.domain.refreshToken.service;

import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.entity.RefreshToken;
import com.jwj.community.domain.member.service.MemberService;
import com.jwj.community.web.login.request.MemberSaveRequest;
import com.jwj.community.web.login.request.RefreshTokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static com.jwj.community.web.code.jwt.JwtTokenFactory.getJwtToken;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RefreshTokenServiceTest {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private MemberService memberService;

    private Member member;

    @BeforeEach
    public void setup(){
        MemberSaveRequest request = MemberSaveRequest.builder()
                .email("admin@google.com")
                .password("1234")
                .nickname("닉네임")
                .build();

        member = request.toEntity();

        memberService.createMember(member);
    }

    @Test
    @DisplayName("RefreshToken 생성")
    void test1() throws Exception{
        // given
        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .refreshToken(getJwtToken().getRefreshToken())
                .build();
        // when
        Long savedId = refreshTokenService.createRefreshToken(request.toEntity(), member);
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(savedId);
        // then
        assertThat(refreshToken.getToken()).isEqualTo(request.getRefreshToken());
    }

    @Test
    @DisplayName("RefreshToken 회원으로 조회하기")
    void test2() throws Exception{
        // given
        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .refreshToken(getJwtToken().getRefreshToken())
                .build();
        // when
        refreshTokenService.createRefreshToken(request.toEntity(), member);
        RefreshToken refreshToken = refreshTokenService.getRefreshTokenByMember(member);
        // then
        assertThat(refreshToken.getToken()).isEqualTo(request.getRefreshToken());
    }
}