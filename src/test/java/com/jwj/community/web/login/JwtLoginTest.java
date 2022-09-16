package com.jwj.community.web.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwj.community.domain.member.service.MemberService;
import com.jwj.community.web.login.request.MemberSaveRequest;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc // SpringBootTest와 MockMvc 주입을 같이할 때 사용!
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-mockmvc.html 참고
public class JwtLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    LoginTestDTO loginTestDTO = LoginTestDTO.builder()
            .email("admin@google.com")
            .password("1234")
            .build();

    @BeforeEach
    public void setup(){
        MemberSaveRequest request = MemberSaveRequest.builder()
                .email("admin@google.com")
                .password("1234")
                .nickname("닉네임")
                .build();

        memberService.createMember(request.toEntity());
    }

    @Test
    @DisplayName("Jwt 인증 요청 성공")
    public void test1() throws Exception{
        // given
        byte[] loginTestDTOByte = objectMapper.writeValueAsString(loginTestDTO).getBytes(UTF_8);

        // expected
        mockMvc.perform(post("/api/login")
                .content(loginTestDTOByte)
                .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString())
                .andDo(print());

    }

    @Test
    @DisplayName("Jwt 인증 요청 아이디 없을 때")
    public void test2() throws Exception{
        // given
        loginTestDTO.setEmail("");
        byte[] loginTestDTOByte = objectMapper.writeValueAsString(loginTestDTO).getBytes(UTF_8);

        // expected
        mockMvc.perform(post("/api/login")
                .content(loginTestDTOByte)
                .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("401"))
                .andExpect(jsonPath("$.errorMessage").value("이메일을 확인 해 주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("Jwt 인증 요청 아이디 null일 때")
    public void test3() throws Exception{
        // given
        loginTestDTO.setEmail(null);
        byte[] loginTestDTOByte = objectMapper.writeValueAsString(loginTestDTO).getBytes(UTF_8);

        // expected
        mockMvc.perform(post("/api/login")
                .content(loginTestDTOByte)
                .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("401"))
                .andExpect(jsonPath("$.errorMessage").value("이메일을 확인 해 주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("Jwt 인증 요청 비밀번호 없을 때")
    public void test4() throws Exception{
        // given
        loginTestDTO.setPassword("");
        byte[] loginTestDTOByte = objectMapper.writeValueAsString(loginTestDTO).getBytes(UTF_8);

        // expected
        mockMvc.perform(post("/api/login")
                .content(loginTestDTOByte)
                .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("401"))
                .andExpect(jsonPath("$.errorMessage").value("비밀번호를 확인 해 주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("Jwt 인증 요청 비밀번호 null일 때")
    public void test5() throws Exception{
        // given
        loginTestDTO.setPassword(null);
        byte[] loginTestDTOByte = objectMapper.writeValueAsString(loginTestDTO).getBytes(UTF_8);

        // expected
        mockMvc.perform(post("/api/login")
                .content(loginTestDTOByte)
                .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("401"))
                .andExpect(jsonPath("$.errorMessage").value("비밀번호를 확인 해 주세요."))
                .andDo(print());
    }

    @Data
    @Builder
    static class LoginTestDTO{
        private String email;
        private String password;
    }
}
