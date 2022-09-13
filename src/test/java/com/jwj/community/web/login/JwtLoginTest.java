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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
                .with(csrf().asHeader())
                .header("X-Requested-With", "XMLHttpRequest")
                .content(loginTestDTOByte)
                .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("admin@google.com"))
                .andDo(print());
    }

    @Data
    @Builder
    static class LoginTestDTO{
        private String email;
        private String password;
    }
}
