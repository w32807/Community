package com.jwj.community.domain.board.repository.impl;

import com.jwj.community.domain.board.repository.BoardQueryRepository;
import com.jwj.community.domain.entity.Board;
import com.jwj.community.web.condition.BoardSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jwj.community.domain.entity.QBoard.board;
import static com.jwj.community.domain.entity.QMember.member;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepositoryImpl implements BoardQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Board> getBoards(BoardSearchCondition condition) {

        // type은 글제목, 글내용, 작성자, 조합 3가지
        return queryFactory
                .selectFrom(board)
                .leftJoin(board.member, member)
                .where(searchCondition(condition))
                .fetchJoin()
                .limit(condition.getSize())
                .offset(condition.getOffset())
                .orderBy(board.id.desc())
                .fetch();
    }

    private BooleanBuilder searchCondition(BoardSearchCondition condition){
        if(!hasText(condition.getKeyword())) return new BooleanBuilder();

        String keyword = condition.getKeyword();
        String searchType = condition.getSearchType().toUpperCase();

        BooleanBuilder result = new BooleanBuilder();

        if(searchType.contains("T")){
            result.or(board.title.containsIgnoreCase(keyword));
        }

        if(searchType.contains("C")){
            result.or(board.content.containsIgnoreCase(keyword));
        }

        if(searchType.contains("W")){
            result.or(member.nickname.containsIgnoreCase(keyword));
        }

        return result;
    }
}
