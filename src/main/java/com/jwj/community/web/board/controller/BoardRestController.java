package com.jwj.community.web.board.controller;

import com.jwj.community.domain.board.service.BoardService;
import com.jwj.community.domain.member.service.MemberService;
import com.jwj.community.web.board.dto.request.BoardSaveRequest;
import com.jwj.community.web.board.dto.request.BoardUpdateRequest;
import com.jwj.community.web.board.dto.response.BoardResponse;
import com.jwj.community.web.common.result.ListResult;
import com.jwj.community.web.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardRestController {

    private final MemberService memberService;
    private final BoardService boardService;

    @GetMapping("/boards")
    public ResponseEntity<ListResult<BoardResponse>> boards(){
        List<BoardResponse> boards = boardService.getBoards().stream()
                .map(board -> board.toResponse())
                .collect(toList());

        ListResult<BoardResponse> listResult = ListResult.<BoardResponse>builder()
                .list(boards)
                .build();

        return new ResponseEntity<>(listResult, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<BoardResponse>> board(@PathVariable("id") Long id){
        BoardResponse board = boardService.getBoard(id).toResponse();

        Result<BoardResponse> result = Result.<BoardResponse>builder()
                .data(board)
                .build();

        return new ResponseEntity<>(result, OK);
    }

    @PostMapping
    public ResponseEntity<Result> addBoard(@RequestBody @Valid BoardSaveRequest request){
        Result<Long> result = Result.<Long>builder()
                .data(boardService.addBoard(request.toEntity(), memberService.findByEmail(request.getEmail())))
                .build();

        return new ResponseEntity<>(result, OK);
    }

    @PutMapping
    public ResponseEntity<Result> updateBoard(@RequestBody @Valid BoardUpdateRequest request){
        Result<Long> result = Result.<Long>builder()
                .data(boardService.updateBoard(request.toEntity()))
                .build();

        return new ResponseEntity<>(result, OK);
    }

    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable("id") Long id){
        boardService.deleteBoard(id);
    }

}
