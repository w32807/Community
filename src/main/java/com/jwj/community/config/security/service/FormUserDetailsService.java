package com.jwj.community.config.security.service;

import com.jwj.community.config.security.config.LoginContext;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

/**
 * 사용자 정보를 확인하는 UserDetailsService 클래스
 */
@Service
@RequiredArgsConstructor
public class FormUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }

        return new LoginContext(member,
                member.getRoleSet()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(toList()));
    }

}
