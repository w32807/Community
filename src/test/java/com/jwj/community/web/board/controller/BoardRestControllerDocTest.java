package com.jwj.community.web.board.controller;

import com.jwj.community.domain.board.service.BoardService;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.member.repository.MemberRepository;
import com.jwj.community.web.board.dto.request.BoardSaveRequest;
import com.jwj.community.web.login.request.MemberSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BoardRestControllerDocTest {

    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardService boardService;

    private String saveMemberEmail = "admin@google.com";

    private Member savedMember;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email(saveMemberEmail)
                .password("1234")
                .build();

        savedMember = memberRepository.save(memberSaveRequest.toEntity());
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
        this.mockMvc.perform(MockMvcRequestBuilders.get("/board/{id}", 1L)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("index"));
    }

}
