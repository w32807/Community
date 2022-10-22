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
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static com.jwj.community.web.member.jwt.JwtConst.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private MessageSource messageSource;

    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    @Autowired
    private ObjectMapper objectMapper;

    private String saveMemberEmail = "admin@google.com";

    private String saveMemberNickname = "nickname";

    private Member savedMember;

    private String accessToken;

    @BeforeEach
    void setup(){
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email(saveMemberEmail)
                .nickname(saveMemberNickname)
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

    @Test
    @DisplayName("회원 등록 시 이메일 형식을 지켜야 한다.")
    void test2() throws Exception{
        // given
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email("이메일 형식이 아님!")
                .password("1234")
                .confirmPassword("1234")
                .nickname("test nickname")
                .build();

        // expected
        mockMvc.perform(post("/api/member/addMember")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(memberSaveRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.fieldErrors.[0].field").value("email"))
                .andExpect(jsonPath("$.fieldErrors.[0].message").value(messageSource.getMessage("field.required.emailFormat", null, Locale.getDefault())))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 등록 시 이메일은 필수 입력이다.")
    void test3() throws Exception{
        // given
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .password("1234")
                .confirmPassword("1234")
                .nickname("test nickname")
                .build();

        // expected
        mockMvc.perform(post("/api/member/addMember")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(memberSaveRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.fieldErrors.[0].field").value("email"))
                .andExpect(jsonPath("$.fieldErrors.[0].message").value(messageSource.getMessage("field.required.email", null, Locale.getDefault())))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 등록 시 비밀번호는 필수 입력이다.")
    void test4() throws Exception{
        // given
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email("admin@google.com")
                .confirmPassword("1234")
                .nickname("test nickname")
                .build();

        // expected
        mockMvc.perform(post("/api/member/addMember")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(memberSaveRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.fieldErrors.[0].field").value("password"))
                .andExpect(jsonPath("$.fieldErrors.[0].message").value(messageSource.getMessage("field.required.password", null, Locale.getDefault())))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 등록 시 비밀번호 확인 필수 입력이다.")
    void test5() throws Exception{
        // given
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email("admin@google.com")
                .password("1234")
                .nickname("test nickname")
                .build();

        // expected
        mockMvc.perform(post("/api/member/addMember")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(memberSaveRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.fieldErrors.[0].field").value("confirmPassword"))
                .andExpect(jsonPath("$.fieldErrors.[0].message").value(messageSource.getMessage("field.required.confirmPassword", null, Locale.getDefault())))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 등록 시 닉네임은 필수 입력이다.")
    void test6() throws Exception{
        // given
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email("admin@google.com")
                .password("1234")
                .confirmPassword("1234")
                .build();

        // expected
        mockMvc.perform(post("/api/member/addMember")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(memberSaveRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.fieldErrors.[0].field").value("nickname"))
                .andExpect(jsonPath("$.fieldErrors.[0].message").value(messageSource.getMessage("field.required.nickname", null, Locale.getDefault())))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 등록 시 비밀번호와 비밀번호 확인은 동일해야 한다.")
    void test7() throws Exception{
        // given
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email("admin@google.com")
                .password("1234")
                .confirmPassword("1234124512412412")
                .nickname("test nickname")
                .build();

        // expected
        mockMvc.perform(post("/api/member/addMember")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(memberSaveRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 등록 시 이메일은 중복되면 안된다.")
    void test8() throws Exception{
        // given
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email(saveMemberEmail)
                .password("1234")
                .confirmPassword("1234")
                .nickname("test nickname")
                .build();

        // expected
        mockMvc.perform(post("/api/member/addMember")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(memberSaveRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andDo(print());
    }


    @Test
    @DisplayName("회원 등록 시 닉네임은 중복되면 안된다.")
    void test9() throws Exception{
        // given
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email("admin@google.com")
                .password("1234")
                .confirmPassword("1234124512412412")
                .nickname(saveMemberNickname)
                .build();

        // expected
        mockMvc.perform(post("/api/member/addMember")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(memberSaveRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andDo(print());
    }
}