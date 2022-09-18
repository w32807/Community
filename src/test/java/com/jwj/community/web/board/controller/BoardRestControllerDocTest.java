package com.jwj.community.web.board.controller;

import com.jwj.community.domain.board.service.BoardService;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.member.repository.MemberRepository;
import com.jwj.community.web.board.dto.request.BoardSaveRequest;
import com.jwj.community.web.code.jwt.JwtTokenFactory;
import com.jwj.community.web.login.request.MemberSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static com.jwj.community.web.login.jwt.JwtConst.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.community.com", uriPort = 443)
@AutoConfigureMockMvc
public class BoardRestControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    private String saveMemberEmail = "admin@google.com";

    private Member savedMember;

    private String jwtAccessToken;

    @BeforeEach
    public void setUp() {
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email(saveMemberEmail)
                .password("1234")
                .build();

        savedMember = memberRepository.save(memberSaveRequest.toEntity());
        jwtAccessToken = jwtTokenFactory.getRequestJwtToken().getAccessToken();
    }

    @Test
    @DisplayName("아무개")
    void test1() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목1")
                .content("글 내용1")
                .build();

        boardService.addBoard(boardSaveRequest.toEntity(), savedMember);

        // expected
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/board/{id}", 1L)
                .header(AUTHORIZATION, jwtAccessToken)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("index"));
    }

}
