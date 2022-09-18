package com.jwj.community.domain.member.service;

import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.member.repository.MemberRepository;
import com.jwj.community.web.login.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jwj.community.domain.common.enums.Roles.ROLE_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long createMember(Member member){
        member.changePassword(encode(member.getPassword()));
        member.addRole(ROLE_MEMBER);

        memberRepository.save(member);
        return member.getId();
    }

    public void changeRefreshToken(Member member, JwtToken jwtToken) {
        Member savedMember = findByEmail(member.getEmail());
        //savedMember.changeRefreshToken(jwtToken.getRefreshToken());
    }

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email);
    }

    public Member findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
    }

    private String encode(String password){
        return passwordEncoder.encode(password);
    }

}
