package com.jwj.community.domain.refreshToken.repository;

import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.entity.RefreshToken;

public interface RefreshTokenQueryRepository {

    RefreshToken findByMember(Member member);

}
