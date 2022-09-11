package com.jwj.community.domain.member.service;

import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.jwj.community.domain.common.enums.Roles.ROLE_MEMBER;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long createMember(Member member){
        member.changePassword(encode(member.getPassword()));
        member.addRole(ROLE_MEMBER);

        memberRepository.save(member);
        return member.getId();
    }

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email);
    }

    private String encode(String password){
        return passwordEncoder.encode(password);
    }

}
