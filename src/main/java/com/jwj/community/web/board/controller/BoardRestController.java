package com.jwj.community.web.board.controller;

import com.jwj.community.domain.board.service.BoardService;
import com.jwj.community.domain.member.service.MemberService;
import com.jwj.community.web.annotation.LoginMember;
import com.jwj.community.web.board.dto.request.BoardSaveRequest;
import com.jwj.community.web.board.dto.request.BoardUpdateRequest;
import com.jwj.community.web.board.dto.response.BoardResponse;
import com.jwj.community.web.common.result.ListResult;
import com.jwj.community.web.common.result.Result;
import com.jwj.community.web.condition.BoardSearchCondition;
import com.jwj.community.web.member.dto.LoginMemberDTO;
import com.jwj.community.web.member.dto.response.WriteMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.jwj.community.web.board.dto.response.BoardResponse.of;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardRestController {

    private final MemberService memberService;
    private final BoardService boardService;

    @GetMapping("/boards")
    public ResponseEntity<ListResult<BoardResponse>> boards(BoardSearchCondition condition){
        // todo Pageable로 페이징 기능 구현해야 됨
        List<BoardResponse> boards = boardService.getBoards(condition).stream()
                .map(board -> {
                    BoardResponse response = of(board);
                    response.setMember(WriteMemberResponse.of(board.getMember()));
                    return response;
                })
                .collect(toList());

        ListResult<BoardResponse> listResult = ListResult.<BoardResponse>builder()
                .list(boards)
                .build();

        return ok().body(listResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<BoardResponse>> board(@PathVariable("id") Long id){
        Result<BoardResponse> result = Result.<BoardResponse>builder()
                .data(of(boardService.getBoard(id)))
                .build();

        return ok().body(result);
    }

    @PostMapping("/board")
    public ResponseEntity<Result> addBoard(@RequestBody @Valid BoardSaveRequest request, @LoginMember LoginMemberDTO loginMember){
        Result<Long> result = Result.<Long>builder()
                .data(boardService.addBoard(request.toEntity(), memberService.findByEmail(loginMember.getEmail())))
                .build();

        return ok().body(result);
    }

    @PutMapping("/board")
    public ResponseEntity<Result> updateBoard(@RequestBody @Valid BoardUpdateRequest request){
        Result<Long> result = Result.<Long>builder()
                .data(boardService.updateBoard(request.toEntity()))
                .build();

        return ok().body(result);
    }

    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable("id") Long id){
        boardService.deleteBoard(id);
    }

}
