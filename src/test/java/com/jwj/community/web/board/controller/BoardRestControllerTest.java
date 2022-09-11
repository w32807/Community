package com.jwj.community.web.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwj.community.domain.board.service.BoardService;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.member.repository.MemberRepository;
import com.jwj.community.web.board.dto.request.BoardSaveRequest;
import com.jwj.community.web.board.dto.request.BoardUpdateRequest;
import com.jwj.community.web.login.request.MemberSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // SpringBootTest와 MockMvc 주입을 같이할 때 사용!
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BoardRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private MessageSource messageSource;

    private String saveMemberEmail = "admin@google.com";

    private Member savedMember;

    @Autowired
    private ObjectMapper objectMapper;

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

        // expected
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
    }

    @Test
    @DisplayName("글 여러 개 조회 시 글이 없을 때")
    @WithMockUser
    void test2() throws Exception{
        // given

        // expected
        mockMvc.perform(get("/board/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(0))
                .andExpect(jsonPath("$.list.length()").value(0))
                .andDo(print());
    }

    @Test
    @DisplayName("글 1개 조회하기")
    @WithMockUser
    void test3() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목1")
                .content("글 내용1")
                .build();

        boardService.addBoard(boardSaveRequest.toEntity(), savedMember);

        // expected
        mockMvc.perform(get("/board/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("글 제목1"))
                .andExpect(jsonPath("$.data.content").value("글 내용1"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 1개 조회 시 글 존재하지 않음")
    @WithMockUser
    void test4() throws Exception{
        // given
        Long id = -1L;
        // expected
        mockMvc.perform(get("/board/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value(messageSource.getMessage("error.noBoard", null, null)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 1개 삭제")
    @WithMockUser
    void test5() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목1")
                .content("글 내용1")
                .build();

        Long id = 1L;

        boardService.addBoard(boardSaveRequest.toEntity(), savedMember);

        // expected
        mockMvc.perform(delete("/board/{id}", id)
                .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/board/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value(messageSource.getMessage("error.noBoard", null, null)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 저장 성공")
    @WithMockUser
    void test6() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목1")
                .content("글 내용1")
                .email(saveMemberEmail)
                .build();

        // expected
        mockMvc.perform(post("/board")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardSaveRequest))
                .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1L))
                .andDo(print());
    }

    @Test
    @DisplayName("글 저장 시 글 제목은 필수입력")
    @WithMockUser
    void test7() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .content("글 내용1")
                .email(saveMemberEmail)
                .build();

        // expected
        mockMvc.perform(post("/board")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardSaveRequest))
                .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.fieldErrors.[0].field").value("title"))
                .andExpect(jsonPath("$.fieldErrors.[0].message").value(messageSource.getMessage("field.required.title", null, null)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 저장 시 글 내용은 필수입력")
    @WithMockUser
    void test8() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목1")
                .email(saveMemberEmail)
                .build();

        // expected
        mockMvc.perform(post("/board")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardSaveRequest))
                .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.fieldErrors.[0].field").value("content"))
                .andExpect(jsonPath("$.fieldErrors.[0].message").value(messageSource.getMessage("field.required.content", null, null)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 성공")
    @WithMockUser
    void test9() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목1")
                .content("글 내용1")
                .build();

        Long saveId = boardService.addBoard(boardSaveRequest.toEntity(), savedMember);

        BoardUpdateRequest boardUpdateRequest = BoardUpdateRequest.builder()
                .id(saveId)
                .title("수정한 글 제목1")
                .content("수정한 내용1")
                .build();

        // expected
        mockMvc.perform(put("/board")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardUpdateRequest))
                .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(saveId))
                .andDo(print());

        mockMvc.perform(get("/board/{id}", saveId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(saveId))
                .andExpect(jsonPath("$.data.title").value("수정한 글 제목1"))
                .andExpect(jsonPath("$.data.content").value("수정한 내용1"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 시 글 제목은 필수입력")
    @WithMockUser
    void test10() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목1")
                .content("글 내용1")
                .build();

        Long saveId = boardService.addBoard(boardSaveRequest.toEntity(), savedMember);

        BoardUpdateRequest boardUpdateRequest = BoardUpdateRequest.builder()
                .id(saveId)
                .content("수정한 내용1")
                .build();

        // expected
        mockMvc.perform(put("/board")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardUpdateRequest))
                .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.fieldErrors.[0].field").value("title"))
                .andExpect(jsonPath("$.fieldErrors.[0].message").value(messageSource.getMessage("field.required.title", null, null)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 시 글 내용은 필수입력")
    @WithMockUser
    void test11() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목1")
                .content("글 내용1")
                .build();

        Long saveId = boardService.addBoard(boardSaveRequest.toEntity(), savedMember);

        BoardUpdateRequest boardUpdateRequest = BoardUpdateRequest.builder()
                .id(saveId)
                .title("수정한 글 제목1")
                .build();

        // expected
        mockMvc.perform(put("/board")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardUpdateRequest))
                .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.fieldErrors.[0].field").value("content"))
                .andExpect(jsonPath("$.fieldErrors.[0].message").value(messageSource.getMessage("field.required.content", null, null)))
                .andDo(print());
    }


}
