package com.jwj.community.config.security.config;

import com.jwj.community.config.security.common.AjaxLoginAuthenticationEntryPoint;
import com.jwj.community.config.security.filter.AjaxLoginProcessingFilter;
import com.jwj.community.config.security.handler.AjaxAccessDeniedHandler;
import com.jwj.community.config.security.handler.AjaxAuthenticationFailureHandler;
import com.jwj.community.config.security.handler.AjaxAuthenticationSuccessHandler;
import com.jwj.community.config.security.provider.AjaxAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

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

        http.addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                .authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint())
                .accessDeniedHandler(new AjaxAccessDeniedHandler());

        return http.build();
    }

    @Bean
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() {
        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManager());
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());

        return ajaxLoginProcessingFilter;
    }

    /**
     * Form 인증은 스프링 시큐리티에서 제공하는 UsernamePasswordAuthenticationFilter를 사용하기 때문에
     * http API를 사용하여 provider를 등록하면 ProviderManager에 추가 해 준다.
     * 그러나 Ajax 방식은 우리가 추가한 AjaxLoginProcessingFilter에 AuthenticationManager를 등록 해 주어야 하기 때문에
     * ProviderManager를 만들어 주어야 하고, 그 ProviderManager에 AjaxAuthenticationProvider를 추가 후
     * AjaxLoginProcessingFilter에 등록하여야 한다.
     * @return
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
        authenticationProviders.add(new AjaxAuthenticationProvider(userDetailsService, passwordEncoder()));
        return new ProviderManager(authenticationProviders);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
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
