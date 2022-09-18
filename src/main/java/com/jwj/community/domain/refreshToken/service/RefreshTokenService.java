package com.jwj.community.domain.refreshToken.service;

import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.entity.RefreshToken;
import com.jwj.community.domain.member.repository.MemberRepository;
import com.jwj.community.domain.refreshToken.repository.RefreshTokenRepository;
import com.jwj.community.web.exception.exceptions.refreshToken.RefreshTokenNotFound;
import com.jwj.community.web.login.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Locale.getDefault;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MessageSource messageSource;

    public Long createRefreshToken(RefreshToken refreshToken, Member member) {
        member.changeRefreshToken(refreshToken);
        refreshToken.setMember(member);
        return refreshTokenRepository.save(refreshToken).getId();
    }

    public void changeRefreshToken(String email, JwtToken jwtToken) {
        Member savedMember = memberRepository.findByEmail(email);
        refreshTokenRepository.findByMember(savedMember);
    }

    public RefreshToken getRefreshToken(Long id) throws RefreshTokenNotFound {
        return refreshTokenRepository.findById(id)
            .orElseThrow(() -> new RefreshTokenNotFound(messageSource.getMessage("error.noRefreshToken", null, getDefault())));
    }

    public RefreshToken getRefreshTokenByMember(Member member) throws RefreshTokenNotFound {
        return refreshTokenRepository.findByMember(member);
    }
}
