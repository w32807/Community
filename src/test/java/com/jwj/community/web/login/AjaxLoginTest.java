package com.jwj.community.web.login;

import com.jwj.community.domain.member.service.MemberService;
import com.jwj.community.web.login.request.MemberSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static java.net.URLEncoder.encode;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // SpringBootTest와 MockMvc 주입을 같이할 때 사용!
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-mockmvc.html 참고
public class AjaxLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

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
    @DisplayName("폼 로그인 성공")
    public void test1() throws Exception{
        // given
        String loginEmail = "admin@google.com";
        String loginPassword = "1234";

        // when
        // then
        mockMvc.perform(formLogin("/login/loginProcess")
                .userParameter("email")
                .passwordParam("password")
                .user(loginEmail)
                .password(loginPassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main/home"))
                .andExpect(authenticated().withRoles("MEMBER"));

    }

    @Test
    @DisplayName("폼 로그인 존재하지 않는 아이디 입력했을 때")
    public void test2() throws Exception{
        // given
        String loginEmail = "존재하지 않는 아이디";
        String loginPassword = "1234";

        // when
        // then
        mockMvc.perform(formLogin("/login/loginProcess")
                .userParameter("email")
                .passwordParam("password")
                .user(loginEmail)
                .password(loginPassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login/loginFail?error=true&exception=" + encode("이메일을 확인 해 주세요", "UTF-8")))
                .andExpect(unauthenticated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("폼 로그인 아이디는 맞는데 비밀번호 틀렸을 때")
    public void test3() throws Exception{
        // given
        String loginEmail = "admin@google.com";
        String loginPassword = "틀린 비밀번호";

        // when
        // then
        mockMvc.perform(formLogin("/login/loginProcess")
                .userParameter("email")
                .passwordParam("password")
                .user(loginEmail)
                .password(loginPassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login/loginFail?error=true&exception=" + encode("비밀번호를 확인 해 주세요", "UTF-8")))
                .andExpect(unauthenticated());
    }

}
