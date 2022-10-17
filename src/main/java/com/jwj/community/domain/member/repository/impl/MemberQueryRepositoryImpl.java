package com.jwj.community.domain.member.repository.impl;

import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.member.repository.MemberQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import static com.jwj.community.domain.entity.QMember.member;
import static com.jwj.community.utils.CommonUtils.nullSafeBuilder;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Member findByEmail(String email) throws UsernameNotFoundException {
        return queryFactory
                .selectFrom(member)
                .where(emailEq(email))
                .fetchOne();
    }

    @Override
    public Member findByNick(String nick) {
        return queryFactory
                .selectFrom(member)
                .where(nickEq(nick))
                .fetchOne();
    }

    private BooleanBuilder emailEq(String email) {
        return nullSafeBuilder(() -> member.email.eq(email));
    }

    private BooleanBuilder nickEq(String nick) {
        return nullSafeBuilder(() -> member.nickname.eq(nick));
    }

}
