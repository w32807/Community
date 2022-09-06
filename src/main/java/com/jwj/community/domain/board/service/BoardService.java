package com.jwj.community.domain.board.service;

import com.jwj.community.domain.board.repository.BoardRepository;
import com.jwj.community.domain.entity.Board;
import com.jwj.community.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void addBoard(Board board, Member member){
        board.setMember(member);
        boardRepository.save(board);
    }
}
