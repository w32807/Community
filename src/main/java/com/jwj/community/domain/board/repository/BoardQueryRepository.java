package com.jwj.community.domain.board.repository;

import com.jwj.community.domain.entity.Board;
import com.jwj.community.web.condition.BoardSearchCondition;
import org.springframework.data.domain.Page;

public interface BoardQueryRepository {

    Page<Board> getBoards(BoardSearchCondition condition);
}
