package com.jwj.community.domain.refreshToken.repository.impl;

import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.entity.RefreshToken;
import com.jwj.community.domain.refreshToken.repository.RefreshTokenQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.jwj.community.domain.entity.QRefreshToken.refreshToken;
import static com.jwj.community.utils.CommonUtils.nullSafeBuilder;

@Repository
@RequiredArgsConstructor
public class RefreshTokenQueryRepositoryImpl implements RefreshTokenQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public RefreshToken findByMember(Member member) {

        return queryFactory.selectFrom(refreshToken)
                .where(memberEq(member))
                .fetchOne();
    }

    private BooleanBuilder memberEq(Member member) {
        return nullSafeBuilder(() -> refreshToken.member.eq(member));
    }
}
