package com.jwj.community.domain.refreshToken.repository;

import com.jwj.community.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenQueryRepository {
}
