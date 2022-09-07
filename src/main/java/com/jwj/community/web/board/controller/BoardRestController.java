package com.jwj.community.web.board.controller;

import com.jwj.community.domain.board.service.BoardService;
import com.jwj.community.web.board.dto.response.BoardResponse;
import com.jwj.community.web.common.result.ListResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardRestController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public ResponseEntity<ListResult<BoardResponse>> boards(){
        List<BoardResponse> boards = boardService.getBoards().stream()
                .map(board -> board.toResponse())
                .collect(toList());

        ListResult<BoardResponse> listResult = ListResult.<BoardResponse>builder()
                .list(boards)
                .build();

        return new ResponseEntity<>(listResult, HttpStatus.OK);
    }


}
