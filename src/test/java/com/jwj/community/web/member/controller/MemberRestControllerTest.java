package com.jwj.community.web.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.member.service.MemberService;
import com.jwj.community.domain.refreshToken.service.RefreshTokenService;
import com.jwj.community.web.code.jwt.JwtTokenFactory;
import com.jwj.community.web.member.dto.request.MemberSaveRequest;
import com.jwj.community.web.refreshToken.dto.request.RefreshTokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.jwj.community.web.member.jwt.JwtConst.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

;

@SpringBootTest
@AutoConfigureMockMvc // SpringBootTest와 MockMvc 주입을 같이할 때 사용!
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    @Autowired
    private ObjectMapper objectMapper;

    private String saveMemberEmail = "admin@google.com";

    private Member savedMember;

    private String accessToken;

    @BeforeEach
    void setup(){
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email(saveMemberEmail)
                .password("1234")
                .build();

        // given
        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .refreshToken(jwtTokenFactory.getJwtToken().getRefreshToken())
                .build();

        savedMember = memberService.findById(memberService.addMember(memberSaveRequest.toEntity()));
        refreshTokenService.createRefreshToken(request.toEntity(), savedMember.getEmail());
        accessToken = jwtTokenFactory.getRequestJwtToken().getAccessToken();
    }

    @Test
    @DisplayName("회원 여러 명 조회하기")
    void test1() throws Exception{
        // given
        MemberSaveRequest memberSaveRequest1 = MemberSaveRequest.builder()
                .email("test1@google.com")
                .password("1234")
                .nickname("test nickname")
                .build();

        MemberSaveRequest memberSaveRequest2 = MemberSaveRequest.builder()
                .email("test2@google.com")
                .password("1234")
                .nickname("test nickname")
                .build();

        memberService.addMember(memberSaveRequest1.toEntity());
        memberService.addMember(memberSaveRequest2.toEntity());

        // expected
        mockMvc.perform(get("/api/member/members")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(3))
                .andExpect(jsonPath("$.list.length()").value(3))
                .andDo(print());
    }

}