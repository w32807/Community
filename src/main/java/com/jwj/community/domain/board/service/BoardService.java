package com.jwj.community.domain.board.service;

import com.jwj.community.domain.board.repository.BoardRepository;
import com.jwj.community.domain.entity.Board;
import com.jwj.community.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MessageSource messageSource;

    public Long addBoard(Board board, Member member){
        board.setMember(member);
        return boardRepository.save(board).getId();
    }

    public Board getBoard(Long id) throws EntityNotFoundException{
        Board savedBoard = getSavedBoard(id);
        savedBoard.addView();

        return savedBoard;
    }

    public List<Board> getBoards(){
        return boardRepository.findAll();
    }

    public void deleteBoard(Long id){
        boardRepository.deleteById(id);
    }

    public Long updateBoard(Board board) throws EntityNotFoundException{
        Board savedBoard = getSavedBoard(board.getId());
        savedBoard.changeTitle(board.getTitle());
        savedBoard.changeContent(board.getContent());

        return savedBoard.getId();
    }

    private Board getSavedBoard(Long id) throws EntityNotFoundException{
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.noBoard", null, null)));
    }
}
