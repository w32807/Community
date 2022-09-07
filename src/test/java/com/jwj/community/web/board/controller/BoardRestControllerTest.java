package com.jwj.community.web.board.controller;

import com.jwj.community.domain.board.service.BoardService;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.member.repository.MemberRepository;
import com.jwj.community.web.board.dto.request.BoardSaveRequest;
import com.jwj.community.web.login.request.MemberSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // SpringBootTest와 MockMvc 주입을 같이할 때 사용!
class BoardRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardService boardService;

    private String saveMemberEmail = "admin@google.com";

    private Member savedMember;

    @BeforeEach
    void setup(){
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email(saveMemberEmail)
                .password("1234")
                .build();

        savedMember = memberRepository.save(memberSaveRequest.toEntity());
    }

    @Test
    @DisplayName("글 여러 개 조회하기")
    @WithMockUser
    void test1() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest1 = BoardSaveRequest.builder()
                .title("글 제목1")
                .content("글 내용1")
                .build();

        BoardSaveRequest boardSaveRequest2 = BoardSaveRequest.builder()
                .title("글 제목2")
                .content("글 내용2")
                .build();

        boardService.addBoard(boardSaveRequest1.toEntity(), savedMember);
        boardService.addBoard(boardSaveRequest2.toEntity(), savedMember);

        // when
        mockMvc.perform(get("/board/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.list.length()").value(2))
                .andExpect(jsonPath("$.list.[0].id").value(1))
                .andExpect(jsonPath("$.list.[0].title").value("글 제목1"))
                .andExpect(jsonPath("$.list.[0].content").value("글 내용1"))
                .andExpect(jsonPath("$.list.[1].id").value(2))
                .andExpect(jsonPath("$.list.[1].title").value("글 제목2"))
                .andExpect(jsonPath("$.list.[1].content").value("글 내용2"))
                .andDo(print());
        // then
    }


}