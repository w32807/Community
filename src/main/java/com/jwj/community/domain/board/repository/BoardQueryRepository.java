package com.jwj.community.domain.board.repository;

import com.jwj.community.domain.entity.Board;

import java.util.List;

public interface BoardQueryRepository {

    List<Board> getBoards();
}
