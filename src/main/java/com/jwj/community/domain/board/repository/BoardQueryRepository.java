package com.jwj.community.domain.board.repository;

import com.jwj.community.domain.entity.Board;
import com.jwj.community.web.condition.BoardSearchCondition;

import java.util.List;

public interface BoardQueryRepository {

    List<Board> getBoards(BoardSearchCondition condition);
}
