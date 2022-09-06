package com.jwj.community.web.board.controller;

import com.jwj.community.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService boardService;

}
