package com.jwj.community.domain.member.service;

import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jwj.community.domain.common.enums.MemberState.ACTIVE;
import static com.jwj.community.domain.common.enums.Roles.ROLE_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long addMember(Member member){
        member.changePassword(encode(member.getPassword()));
        member.addRole(ROLE_MEMBER);

        memberRepository.save(member);
        return member.getId();
    }

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email);
    }

    public Member findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
    }

    public Member findByNickname(String nick) {
        return memberRepository.findByNick(nick);
    }

    public List<Member> getMembers() {
        return memberRepository.findAll();
    }


    public void deActiveMember(Long id) {
        Member saveMember = findById(id);

        if(saveMember.getState() == ACTIVE){
            saveMember.changeState();
        }

    }

    private String encode(String password){
        return passwordEncoder.encode(password);
    }
}
