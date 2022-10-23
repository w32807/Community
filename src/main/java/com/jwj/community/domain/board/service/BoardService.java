package com.jwj.community.domain.board.service;

import com.jwj.community.domain.board.dto.BoardEditor;
import com.jwj.community.domain.board.repository.BoardRepository;
import com.jwj.community.domain.entity.Board;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.web.condition.BoardSearchCondition;
import com.jwj.community.web.exception.exceptions.board.BoardNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Locale.getDefault;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final MessageSource messageSource;

    public Long addBoard(Board board, Member member){
        board.setMember(member);
        return boardRepository.save(board).getId();
    }

    public Board getBoard(Long id) throws BoardNotFound {
        Board savedBoard = getSavedBoard(id);
        savedBoard.addView();

        return savedBoard;
    }

    public Page<Board> getBoards(BoardSearchCondition condition){
        return boardRepository.getBoards(condition);
    }

    public void deleteBoard(Long id){
        Board savedBoard = getSavedBoard(id);
        boardRepository.deleteById(savedBoard.getId());
    }

    public Long updateBoard(Board board) throws BoardNotFound{
        Board savedBoard = getSavedBoard(board.getId());
        BoardEditor.BoardEditorBuilder editorBuilder = savedBoard.toEditor();

        savedBoard.edit(
                editorBuilder
                .title(board.getTitle())
                .content(board.getContent())
                .build());

        return savedBoard.getId();
    }

    private Board getSavedBoard(Long id) throws BoardNotFound {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFound(messageSource.getMessage("error.noBoard", null, getDefault())));
    }
}
