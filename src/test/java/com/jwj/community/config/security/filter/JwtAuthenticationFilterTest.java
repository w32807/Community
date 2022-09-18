package com.jwj.community.config.security.filter;

import com.jwj.community.domain.member.service.MemberService;
import com.jwj.community.web.code.jwt.JwtTokenFactory;
import com.jwj.community.web.login.jwt.JwtToken;
import com.jwj.community.web.login.request.MemberSaveRequest;
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

import static com.jwj.community.web.login.jwt.JwtConst.AUTHORIZATION;
import static java.util.Locale.getDefault;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc // SpringBootTest와 MockMvc 주입을 같이할 때 사용!
class JwtAuthenticationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    @BeforeEach
    public void setup(){
        MemberSaveRequest request = MemberSaveRequest.builder()
                .email("admin@google.com")
                .password("1234")
                .nickname("닉네임")
                .build();

        memberService.createMember(request.toEntity());
    }

/*
    멤버 넣어주고, 토큰을 가지고 요청을 한다.
        토큰이 유효할 때, 만료되었을 때, 등등 테스트를 한다.
*/
    @Test
    @DisplayName("Jwt 인증 요청 성공")
    public void test1() throws Exception{
        // given
        JwtToken jwtToken = jwtTokenFactory.getRequestJwtToken();
        // expected
        mockMvc.perform(get("/api/board/boards")
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, jwtToken.getAccessToken()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Jwt 토큰이 존재하지 않을 때")
    public void test2() throws Exception{
        // expected
        mockMvc.perform(get("/api/board/boards")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(String.valueOf(UNAUTHORIZED.value())))
                .andExpect(jsonPath("$.errorMessage").value(messageSource.getMessage("error.noJwtToken", null, getDefault())))
                .andDo(print());
    }

    @Test
    @DisplayName("Jwt 토큰이 만료되었을 때")
    public void test3() throws Exception{
        // given
        JwtToken jwtToken = jwtTokenFactory.getRequestExpiredJwtToken();
        // expected
        mockMvc.perform(get("/api/board/boards")
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, jwtToken.getAccessToken()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(String.valueOf(UNAUTHORIZED.value())))
                .andExpect(jsonPath("$.errorMessage").value(messageSource.getMessage("error.expiredJwtToken", null, getDefault())))
                .andDo(print());
    }

}