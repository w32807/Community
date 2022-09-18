package com.jwj.community.domain.member.repository;

import com.jwj.community.domain.entity.Member;
import com.jwj.community.web.login.jwt.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {

    default void changeRefreshToken(String email, JwtToken jwtToken){
        Member savedMember = findByEmail(email);
        //savedMember.changeRefreshToken(jwtToken.getRefreshToken());
    }
}
