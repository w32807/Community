package com.jwj.community.domain.board.service;

import com.jwj.community.domain.entity.Board;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.member.repository.MemberRepository;
import com.jwj.community.web.board.dto.request.BoardSaveRequest;
import com.jwj.community.web.exception.exceptions.board.BoardNotFound;
import com.jwj.community.web.member.dto.request.MemberSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BoardServiceTest {

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
    @DisplayName("글 등록하기")
    void test1() {
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목")
                .content("글 내용")
                .build();

        // when
        Long boardId = boardService.addBoard(boardSaveRequest.toEntity(), savedMember);

        // then
        assertThat(boardId).isEqualTo(1L);
    }

    @Test
    @DisplayName("글 1개 조회하기")
    void test2() {
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목")
                .content("글 내용")
                .build();

        // when
        Long boardId = boardService.addBoard(boardSaveRequest.toEntity(), savedMember);
        Board savedBoard = boardService.getBoard(boardId);

        // then
        assertThat(savedBoard.getId()).isEqualTo(1L);
        assertThat(savedBoard.getTitle()).isEqualTo("글 제목");
        assertThat(savedBoard.getContent()).isEqualTo("글 내용");
    }

    @Test
    @DisplayName("글 1개 조회 시 없는 게시글일 때 예외발생")
    void test3() {
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목")
                .content("글 내용")
                .build();

        // when
        Long boardId = boardService.addBoard(boardSaveRequest.toEntity(), savedMember);

        // then
        assertThatThrownBy(() -> boardService.getBoard(boardId + 1)).isInstanceOf(BoardNotFound.class);
    }

    @Test
    @DisplayName("글 여러 개 조회하기")
    void test4() {
        // given
        BoardSaveRequest boardSaveRequest1 = BoardSaveRequest.builder()
                .title("글 제목1")
                .content("글 내용1")
                .build();

        BoardSaveRequest boardSaveRequest2 = BoardSaveRequest.builder()
                .title("글 제목2")
                .content("글 내용2")
                .build();

        // when
        boardService.addBoard(boardSaveRequest1.toEntity(), savedMember);
        boardService.addBoard(boardSaveRequest2.toEntity(), savedMember);

        List<Board> boards = boardService.getBoards(null).getContent();

        // then
        assertThat(boards.size()).isEqualTo(2);
        assertThat(boards.get(0).getTitle()).isEqualTo("글 제목2");
        assertThat(boards.get(0).getContent()).isEqualTo("글 내용2");
        assertThat(boards.get(1).getTitle()).isEqualTo("글 제목1");
        assertThat(boards.get(1).getContent()).isEqualTo("글 내용1");
    }

    @Test
    @DisplayName("글 1개 삭제")
    void test5() {
        // given
        BoardSaveRequest boardSaveRequest = BoardSaveRequest.builder()
                .title("글 제목1")
                .content("글 내용1")
                .build();

        // when
        Long boardId = boardService.addBoard(boardSaveRequest.toEntity(), savedMember);

        boardService.deleteBoard(boardId);

        // then
        assertThatThrownBy(() -> boardService.getBoard(boardId)).isInstanceOf(BoardNotFound.class);
    }

}