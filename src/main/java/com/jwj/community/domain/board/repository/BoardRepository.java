package com.jwj.community.domain.board.repository;

import com.jwj.community.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
