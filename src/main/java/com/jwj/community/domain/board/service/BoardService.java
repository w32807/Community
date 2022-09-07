package com.jwj.community.domain.board.service;

import com.jwj.community.domain.board.repository.BoardRepository;
import com.jwj.community.domain.entity.Board;
import com.jwj.community.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Long addBoard(Board board, Member member){
        board.setMember(member);
        return boardRepository.save(board).getId();
    }

    public Board getBoard(Long id) throws EntityNotFoundException{
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    public List<Board> getBoards(){
        return boardRepository.findAll();
    }

    public void deleteBoard(Long id){
        boardRepository.deleteById(id);
    }
}
