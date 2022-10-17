package com.jwj.community.web.refreshToken.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.entity.RefreshToken;
import com.jwj.community.domain.member.service.MemberService;
import com.jwj.community.domain.refreshToken.service.RefreshTokenService;
import com.jwj.community.web.code.jwt.JwtTokenFactory;
import com.jwj.community.web.member.jwt.JwtToken;
import com.jwj.community.web.member.dto.request.MemberSaveRequest;
import com.jwj.community.web.refreshToken.dto.request.RefreshTokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Locale.getDefault;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc // SpringBootTest와 MockMvc 주입을 같이할 때 사용!
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RefreshTokenRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageSource messageSource;

    private String refreshToken;

    @BeforeEach
    void setup(){
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email("admin@google.com")
                .password("1234")
                .build();

        // given
        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .refreshToken(jwtTokenFactory.getJwtToken().getRefreshToken())
                .build();

        Member savedMember = memberService.findById(memberService.createMember(memberSaveRequest.toEntity()));
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(refreshTokenService.createRefreshToken(request.toEntity(), savedMember.getEmail()));

        this.refreshToken = refreshToken.getToken();
    }

    @Test
    @DisplayName("만료된 토큰 재발급하기")
    void test1() throws Exception{
        // given
        JwtToken expiredRequestJwtToken = jwtTokenFactory.getExpiredRequestJwtToken();
        expiredRequestJwtToken.setRefreshToken(refreshToken);

        objectMapper.writeValueAsString(expiredRequestJwtToken);

        // expected
        mockMvc.perform(post("/api/refresh/refresh")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(expiredRequestJwtToken)))
                .andExpect(jsonPath("$.data.accessToken").isString())
                .andExpect(jsonPath("$.data.refreshToken").isString())
                .andDo(print());
    }

    @Test
    @DisplayName("refreshToken이 만료되었다면 로그인 페이지로 이동하도록 응답")
    void test2() throws Exception{
        // given
        JwtToken expiredRequestJwtToken = jwtTokenFactory.getExpiredRequestJwtToken();

        objectMapper.writeValueAsString(expiredRequestJwtToken);

        // expected
        mockMvc.perform(post("/api/refresh/refresh")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(expiredRequestJwtToken)))
                .andExpect(jsonPath("$.errorCode").value("401"))
                .andExpect(jsonPath("$.errorMessage").value(messageSource.getMessage("error.expiredJwtToken", null, getDefault())))
                .andDo(print());
    }

}