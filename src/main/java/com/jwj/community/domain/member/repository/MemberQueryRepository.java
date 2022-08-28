package com.jwj.community.domain.member.repository;

import com.jwj.community.domain.entity.Member;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MemberQueryRepository {

    Member findByEmail(String email) throws UsernameNotFoundException;

}
