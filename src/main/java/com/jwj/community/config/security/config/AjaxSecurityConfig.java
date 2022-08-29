package com.jwj.community.config.security.config;

import com.jwj.community.config.security.filter.AjaxLoginProcessingFilter;
import com.jwj.community.config.security.handler.AjaxAuthenticationFailureHandler;
import com.jwj.community.config.security.handler.AjaxAuthenticationSuccessHandler;
import com.jwj.community.config.security.provider.AjaxAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(0) // 설정 클래스들이 초기화 될 때 순서를 부여
@RequiredArgsConstructor
public class AjaxSecurityConfig {

    /**
     * 정적리소스 등의 설정은 form 인증 설정을 담당하는 securityConfig 파일에서 관리하므로
     * 여기에서는 별도로 작성하지 않아도 된다.
     */

    private final UserDetailsService userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean(value = "ajaxSecurityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
            .authorizeRequests()
            .anyRequest().authenticated();

        http.authenticationProvider(ajaxAuthenticationProvider());

        http.addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());

        return ajaxLoginProcessingFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider ajaxAuthenticationProvider(){
        return new AjaxAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler(){
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler ajaxAuthenticationFailureHandler(){
        return new AjaxAuthenticationFailureHandler();
    }

}
