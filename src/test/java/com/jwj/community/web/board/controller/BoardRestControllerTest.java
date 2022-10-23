package com.jwj.community.web.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwj.community.domain.board.service.BoardService;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.member.service.MemberService;
import com.jwj.community.domain.refreshToken.service.RefreshTokenService;
import com.jwj.community.web.board.dto.request.BoardSaveRequest;
import com.jwj.community.web.board.dto.request.BoardUpdateRequest;
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
import java.util.stream.IntStream;

import static com.jwj.community.web.member.jwt.JwtConst.AUTHORIZATION;
import static org.hamcrest.Matchers.contains;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // SpringBootTest와 MockMvc 주입을 같이할 때 사용!
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// jsonPath 문서 : https://github.com/json-path/JsonPath
class BoardRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    @Autowired
    private ObjectMapper objectMapper;

    private String saveMemberEmail = "admin@google.com";

    private String saveMemberNickname = "test nickname";

    private Member savedMember;

    private String accessToken;

    @BeforeEach
    void setup(){
        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .email(saveMemberEmail)
                .password("1234")
                .nickname(saveMemberNickname)
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
    @DisplayName("글 여러 개 조회하기")
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
        mockMvc.perform(get("/api/board/boards")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.list.length()").value(2))
                .andExpect(jsonPath("$.list.[0].id").value(2))
                .andExpect(jsonPath("$.list.[0].title").value("글 제목2"))
                .andExpect(jsonPath("$.list.[0].content").value("글 내용2"))
                .andExpect(jsonPath("$.list.[0].member.email").value(saveMemberEmail))
                .andExpect(jsonPath("$.list.[0].member.nickname").value(saveMemberNickname))
                .andExpect(jsonPath("$.list.[1].id").value(1))
                .andExpect(jsonPath("$.list.[1].title").value("글 제목1"))
                .andExpect(jsonPath("$.list.[1].content").value("글 내용1"))
                .andExpect(jsonPath("$.list.[1].member.email").value(saveMemberEmail))
                .andExpect(jsonPath("$.list.[1].member.nickname").value(saveMemberNickname))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러 개 조회 시 글이 없을 때")
    void test2() throws Exception{
        // given

        // expected
        mockMvc.perform(get("/api/board/boards")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(0))
                .andExpect(jsonPath("$.list.length()").value(0))
                .andDo(print());
    }

    @Test
    @DisplayName("글 1개 조회하기")
    void test3() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목1")
                .content("글 내용1")
                .build();

        boardService.addBoard(boardSaveRequest.toEntity(), savedMember);

        // expected
        mockMvc.perform(get("/api/board/{id}", 1)
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("글 제목1"))
                .andExpect(jsonPath("$.data.content").value("글 내용1"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 1개 조회 시 글 존재하지 않음")
    void test4() throws Exception{
        // given
        Long id = -1L;

        // expected
        mockMvc.perform(get("/api/board/{id}", id)
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(String.valueOf(NOT_FOUND.value())))
                .andExpect(jsonPath("$.errorMessage").value(messageSource.getMessage("error.noBoard", null, Locale.getDefault())))
                .andDo(print());
    }

    @Test
    @DisplayName("글 1개 삭제")
    void test5() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목1")
                .content("글 내용1")
                .build();

        Long id = 1L;

        boardService.addBoard(boardSaveRequest.toEntity(), savedMember);

        // expected
        mockMvc.perform(delete("/api/board/{id}", id)
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/api/board/{id}", id))
                .andDo(print());
    }

    @Test
    @DisplayName("글 저장 성공")
    void test6() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목1")
                .content("글 내용1")
                .build();

        // expected
        mockMvc.perform(post("/api/board/board")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardSaveRequest))
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1L))
                .andDo(print());
    }

    @Test
    @DisplayName("글 저장 시 글 제목은 필수입력")
    void test7() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .content("글 내용1")
                .build();

        // expected
        mockMvc.perform(post("/api/board/board")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardSaveRequest))
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(String.valueOf(BAD_REQUEST.value())))
                .andExpect(jsonPath("$.fieldErrors.[0].field").value("title"))
                .andExpect(jsonPath("$.fieldErrors.[0].message").value(messageSource.getMessage("field.required.title", null, Locale.getDefault())))
                .andDo(print());
    }

    @Test
    @DisplayName("글 저장 시 글 내용은 필수입력")
    void test8() throws Exception{
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목1")
                .build();

        // expected
        mockMvc.perform(post("/api/board/board")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardSaveRequest))
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(String.valueOf(BAD_REQUEST.value())))
                .andExpect(jsonPath("$.fieldErrors.[0].field").value("content"))
                .andExpect(jsonPath("$.fieldErrors.[0].message").value(messageSource.getMessage("field.required.content", null, Locale.getDefault())))
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 성공")
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
        mockMvc.perform(put("/api/board/board")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardUpdateRequest))
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(saveId))
                .andDo(print());

        mockMvc.perform(get("/api/board/{id}", saveId)
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(saveId))
                .andExpect(jsonPath("$.data.title").value("수정한 글 제목1"))
                .andExpect(jsonPath("$.data.content").value("수정한 내용1"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 시 글 제목은 필수입력")
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
        mockMvc.perform(put("/api/board/board")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardUpdateRequest))
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(String.valueOf(BAD_REQUEST.value())))
                .andExpect(jsonPath("$.fieldErrors.[0].field").value("title"))
                .andExpect(jsonPath("$.fieldErrors.[0].message").value(messageSource.getMessage("field.required.title", null, Locale.getDefault())))
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 시 글 내용은 필수입력")
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
        mockMvc.perform(put("/api/board/board")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardUpdateRequest))
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(String.valueOf(BAD_REQUEST.value())))
                .andExpect(jsonPath("$.fieldErrors.[0].field").value("content"))
                .andExpect(jsonPath("$.fieldErrors.[0].message").value(messageSource.getMessage("field.required.content", null, Locale.getDefault())))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러 개 조회 시 페이징 - 1번째 페이지")
    void test12() throws Exception{
        // given
        IntStream.rangeClosed(1, 25).forEach(i -> {
            BoardSaveRequest request = BoardSaveRequest.builder()
                    .title("글 제목" + i)
                    .content("글 내용" + i)
                    .build();

            boardService.addBoard(request.toEntity(), savedMember);
        });

        // expected
        mockMvc.perform(get("/api/board/boards?page=1")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$..id").value(contains(25,24,23,22,21,20,19,18,17,16)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러 개 조회 시 페이징 - 2번째 페이지")
    void test13() throws Exception{
        // given
        IntStream.rangeClosed(1, 25).forEach(i -> {
            BoardSaveRequest request = BoardSaveRequest.builder()
                    .title("글 제목" + i)
                    .content("글 내용" + i)
                    .build();

            boardService.addBoard(request.toEntity(), savedMember);
        });

        // expected
        mockMvc.perform(get("/api/board/boards?page=2")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$..id").value(contains(15,14,13,12,11,10,9,8,7,6)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러 개 조회 시 페이징 - 3번째 페이지")
    void test14() throws Exception{
        // given
        IntStream.rangeClosed(1, 25).forEach(i -> {
            BoardSaveRequest request = BoardSaveRequest.builder()
                    .title("글 제목" + i)
                    .content("글 내용" + i)
                    .build();

            boardService.addBoard(request.toEntity(), savedMember);
        });

        // expected
        mockMvc.perform(get("/api/board/boards?page=3")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$..id").value(contains(5,4,3,2,1)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러 개 조회 시 페이징 - 페이지번호 0일때 첫번 째 페이지")
    void test15() throws Exception{
        // given
        IntStream.rangeClosed(1, 25).forEach(i -> {
            BoardSaveRequest request = BoardSaveRequest.builder()
                    .title("글 제목" + i)
                    .content("글 내용" + i)
                    .build();

            boardService.addBoard(request.toEntity(), savedMember);
        });

        // expected
        mockMvc.perform(get("/api/board/boards?page=0")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$..id").value(contains(25,24,23,22,21,20,19,18,17,16)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러 개 조회 시 페이징 - 페이지번호 음수일 때 첫번 째 페이지")
    void test16() throws Exception{
        // given
        IntStream.rangeClosed(1, 25).forEach(i -> {
            BoardSaveRequest request = BoardSaveRequest.builder()
                    .title("글 제목" + i)
                    .content("글 내용" + i)
                    .build();

            boardService.addBoard(request.toEntity(), savedMember);
        });

        // expected
        mockMvc.perform(get("/api/board/boards?page=-100")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$..id").value(contains(25,24,23,22,21,20,19,18,17,16)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러 개 조회 시 페이징 - 글제목으로 조회 및 결과있을 때")
    void test17() throws Exception{
        // given
        IntStream.rangeClosed(1, 25).forEach(i -> {
            BoardSaveRequest request = BoardSaveRequest.builder()
                    .title("글 제목" + i)
                    .content("글 내용" + i)
                    .build();

            boardService.addBoard(request.toEntity(), savedMember);
        });

        // expected
        mockMvc.perform(get("/api/board/boards?page=1&keyword=제목")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$..id").value(contains(25,24,23,22,21,20,19,18,17,16)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러 개 조회 시 페이징 - 글제목으로 조회 및 결과없을 때")
    void test18() throws Exception{
        // given
        IntStream.rangeClosed(1, 25).forEach(i -> {
            BoardSaveRequest request = BoardSaveRequest.builder()
                    .title("글 제목" + i)
                    .content("글 내용" + i)
                    .build();

            boardService.addBoard(request.toEntity(), savedMember);
        });

        // expected
        mockMvc.perform(get("/api/board/boards?page=1&keyword=결과없는 검색어")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(0))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러 개 조회 시 페이징 - 글내용으로 조회 및 결과있을 때")
    void test19() throws Exception{
        // given
        IntStream.rangeClosed(1, 25).forEach(i -> {
            BoardSaveRequest request = BoardSaveRequest.builder()
                    .title("글 제목" + i)
                    .content("글 내용" + i)
                    .build();

            boardService.addBoard(request.toEntity(), savedMember);
        });

        // expected
        mockMvc.perform(get("/api/board/boards?page=1&keyword=4&searchType=C")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(3))
                .andExpect(jsonPath("$..id").value(contains(24, 14, 4)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러 개 조회 시 페이징 - 글내용으로 조회 및 결과없을 때")
    void test20() throws Exception{
        // given
        IntStream.rangeClosed(1, 25).forEach(i -> {
            BoardSaveRequest request = BoardSaveRequest.builder()
                    .title("글 제목" + i)
                    .content("글 내용" + i)
                    .build();

            boardService.addBoard(request.toEntity(), savedMember);
        });

        // expected
        mockMvc.perform(get("/api/board/boards?page=1&keyword=결과없는 검색어&searchType=C")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(0))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러 개 조회 시 페이징 - 작성자로 조회 및 결과있을 때")
    void test21() throws Exception{
        // given
        IntStream.rangeClosed(1, 25).forEach(i -> {
            BoardSaveRequest request = BoardSaveRequest.builder()
                    .title("글 제목" + i)
                    .content("글 내용" + i)
                    .build();

            boardService.addBoard(request.toEntity(), savedMember);
        });

        // expected
        mockMvc.perform(get("/api/board/boards?page=1&keyword=test nickname&searchType=w")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$..id").value(contains(25,24,23,22,21,20,19,18,17,16)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러 개 조회 시 페이징 - 작성자로 조회 및 결과없을 때")
    void test22() throws Exception{
        // given
        IntStream.rangeClosed(1, 25).forEach(i -> {
            BoardSaveRequest request = BoardSaveRequest.builder()
                    .title("글 제목" + i)
                    .content("글 내용" + i)
                    .build();

            boardService.addBoard(request.toEntity(), savedMember);
        });

        // expected
        mockMvc.perform(get("/api/board/boards?page=1&keyword=결과없는 검색어&searchType=w")
                .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(0))
                .andDo(print());
    }
}
