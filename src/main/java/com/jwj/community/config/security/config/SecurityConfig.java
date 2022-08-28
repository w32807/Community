package com.jwj.community.config.security.config;

import com.jwj.community.config.security.handler.FormAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@Order(1)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider formAuthenticationProvider;
    private final AuthenticationDetailsSource authenticationDetailsSource;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        // 정적 리소스에 대한 설정
        return (web) -> web.ignoring().antMatchers("/assets/**", "/css/**", "/img/**", "/js/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/login/**").permitAll()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated();

        http.formLogin()
            .loginPage("/login/loginForm") // 스프링 시큐리티가 제공하는 로그인페이지말고 기본 로그인 페이지 이동 URL 설정 (Controller에 메소드를 만들어 주어야 한다)
            .defaultSuccessUrl("/") // 정상적으로 인증 성공했을 경우 이동하는 URL 설정 (successHandler가 우선순위이며 successHandler에서 리다이렉트 하지 못할 경우 defaultSuccessUrl의 경로로 이동)
            .usernameParameter("email")
            .passwordParameter("password")
            .loginProcessingUrl("/login/loginProcess") // 여기서 설정한 URL이 호출되면 로그인 인증처리를 하는 UsernamePasswordAuthenticationFilter가 실행된다
            .successHandler(successHandler) // successHandler를 우선적용하기 위해서는 defaultSuccessUrl보다 아래에 선언해주어야 한다.
            .failureHandler(failureHandler)
            .authenticationDetailsSource(authenticationDetailsSource)
            .permitAll();

        http.authenticationProvider(formAuthenticationProvider);

        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new FormAccessDeniedHandler();
    }

}
