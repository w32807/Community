package com.jwj.community.config.security.provider;

import com.jwj.community.config.security.config.LoginContext;
import com.jwj.community.config.security.token.AjaxAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Ajax 인증으로 사용자 로그인 시 회원정보, 비밀번호를 체크하는 Provider
 */
@RequiredArgsConstructor
public class AjaxAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();


        LoginContext loginContext = (LoginContext) userDetailsService.loadUserByUsername(username);

        if(!passwordEncoder.matches(password, loginContext.getMember().getPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return new AjaxAuthenticationToken(loginContext.getMember(), null, loginContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 인증 객체가 AjaxAuthenticationToken과 같을 때 authenticate 메소드 호출
        return AjaxAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
