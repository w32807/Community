package com.jwj.community.domain.board.repository.impl;

import com.jwj.community.domain.board.repository.BoardQueryRepository;
import com.jwj.community.domain.entity.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jwj.community.domain.entity.QBoard.board;
import static com.jwj.community.domain.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepositoryImpl implements BoardQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Board> getBoards() {

        return queryFactory
                .selectFrom(board)
                .leftJoin(board.member, member)
                .fetchJoin()
                .fetch();
    }
}
