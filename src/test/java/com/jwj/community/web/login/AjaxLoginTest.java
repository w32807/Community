package com.jwj.community.web.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc // SpringBootTest와 MockMvc 주입을 같이할 때 사용!
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-mockmvc.html 참고
public class AjaxLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    LoginTestDTO loginTestDTO = LoginTestDTO.builder()
            .email("admin@google.com")
            .password("1234")
            .build();

    @Test
    @DisplayName("Ajax 인증 요청 성공")
    public void test1() throws Exception{
        // given
        byte[] loginTestDTOByte = objectMapper.writeValueAsString(loginTestDTO).getBytes(UTF_8);

        // when
        // then
        mockMvc.perform(post("/api/login")
                .with(csrf().asHeader())
                .header("X-Requested-With", "XMLHttpRequest")
                .content(loginTestDTOByte)
                .contentType(APPLICATION_JSON));
    }

    @Test
    @DisplayName("Ajax 요청이 아닐 때 예외발생")
    public void test2() throws Exception{
        // given
        byte[] loginTestDTOByte = objectMapper.writeValueAsString(loginTestDTO).getBytes(UTF_8);

        // when
        // then
        assertThatThrownBy(() -> {
            mockMvc.perform(post("/api/login")
                    .with(csrf().asHeader())
                    .header("X-Requested-With", "Ajax요청이 아님")
                    .content(loginTestDTOByte)
                    .contentType(APPLICATION_JSON));
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Ajax 요청이지만 아이디가 빈값일 때 예외발생")
    public void test3() throws Exception{
        // given
        LoginTestDTO loginTestDTO = LoginTestDTO.builder()
                .email("")
                .password("1234")
                .build();
        byte[] loginTestDTOByte = objectMapper.writeValueAsString(loginTestDTO).getBytes(UTF_8);

        // when
        // then
        assertThatThrownBy(() -> {
            mockMvc.perform(post("/api/login")
                    .with(csrf().asHeader())
                    .header("X-Requested-With", "XMLHttpRequest")
                    .content(loginTestDTOByte)
                    .contentType(APPLICATION_JSON));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Ajax 요청이지만 비밀번호가 빈값일 때 예외발생")
    public void test4() throws Exception{
        // given
        LoginTestDTO loginTestDTO = LoginTestDTO.builder()
                .email("admin@google.com")
                .password("")
                .build();
        byte[] loginTestDTOByte = objectMapper.writeValueAsString(loginTestDTO).getBytes(UTF_8);

        // when
        // then
        assertThatThrownBy(() -> {
            mockMvc.perform(post("/api/login")
                    .with(csrf().asHeader())
                    .header("X-Requested-With", "XMLHttpRequest")
                    .content(loginTestDTOByte)
                    .contentType(APPLICATION_JSON));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Ajax 요청이지만 아이디가 유효하지 않을 때 예외발생")
    public void test5() throws Exception{
        // given
        LoginTestDTO loginTestDTO = LoginTestDTO.builder()
                .password("1234")
                .build();
        byte[] loginTestDTOByte = objectMapper.writeValueAsString(loginTestDTO).getBytes(UTF_8);

        // when
        // then
        assertThatThrownBy(() -> {
            mockMvc.perform(post("/api/login")
                    .with(csrf().asHeader())
                    .header("X-Requested-With", "XMLHttpRequest")
                    .content(loginTestDTOByte)
                    .contentType(APPLICATION_JSON));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Ajax 요청이지만 비밀번호가 유효하지 않을 때 예외발생")
    public void test6() throws Exception{
        // given
        LoginTestDTO loginTestDTO = LoginTestDTO.builder()
                .email("admin@google.com")
                .build();
        byte[] loginTestDTOByte = objectMapper.writeValueAsString(loginTestDTO).getBytes(UTF_8);

        // when
        // then
        assertThatThrownBy(() -> {
            mockMvc.perform(post("/api/login")
                    .with(csrf().asHeader())
                    .header("X-Requested-With", "XMLHttpRequest")
                    .content(loginTestDTOByte)
                    .contentType(APPLICATION_JSON));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("없는 아이디로 인증 시도 시 예외발생")
    public void test7() throws Exception{
        // given
        LoginTestDTO loginTestDTO = LoginTestDTO.builder()
                .email("없는 아이디")
                .password("1234")
                .build();
        byte[] loginTestDTOByte = objectMapper.writeValueAsString(loginTestDTO).getBytes(UTF_8);

        // when
        // then
        mockMvc.perform(post("/api/login")
                .with(csrf().asHeader())
                .header("X-Requested-With", "XMLHttpRequest")
                .content(loginTestDTOByte)
                .contentType(APPLICATION_JSON));
    }

    @Data
    @Builder
    static class LoginTestDTO{
        private String email;
        private String password;
    }
}
