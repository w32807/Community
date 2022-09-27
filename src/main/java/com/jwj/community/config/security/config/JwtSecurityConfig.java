package com.jwj.community.config.security.config;

import com.jwj.community.config.security.common.JwtLoginAuthenticationEntryPoint;
import com.jwj.community.config.security.filter.JwtAuthenticationFilter;
import com.jwj.community.config.security.filter.JwtLoginProcessingFilter;
import com.jwj.community.config.security.handler.JwtAccessDeniedHandler;
import com.jwj.community.config.security.handler.JwtAuthenticationFailureHandler;
import com.jwj.community.config.security.handler.JwtAuthenticationSuccessHandler;
import com.jwj.community.config.security.provider.JwtAuthenticationProvider;
import com.jwj.community.config.security.utils.JwtTokenUtil;
import com.jwj.community.domain.refreshToken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JwtSecurityConfig {

    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenUtil jwtTokenUtil;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        // 정적 리소스에 대한 설정
        return (web) -> web.ignoring().antMatchers("/assets/**", "/css/**", "/img/**", "/js/**");
    }

    @Bean(value = "jwtSecurityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .antMatcher("/api/**")
            .authorizeRequests()
            .antMatchers("/api/login", "/api/refresh/**").permitAll()
            .anyRequest().authenticated();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.csrf().disable();

        http.cors();

        http.addFilterBefore(jwtLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                .authenticationEntryPoint(new JwtLoginAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler());

        return http.build();
    }

    @Bean
    public JwtLoginProcessingFilter jwtLoginProcessingFilter() {
        JwtLoginProcessingFilter jwtLoginProcessingFilter = new JwtLoginProcessingFilter();
        jwtLoginProcessingFilter.setAuthenticationManager(authenticationManager());
        jwtLoginProcessingFilter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler());
        jwtLoginProcessingFilter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler());

        return jwtLoginProcessingFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
        authenticationProviders.add(new JwtAuthenticationProvider(userDetailsService, passwordEncoder()));
        return new ProviderManager(authenticationProviders);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler jwtAuthenticationSuccessHandler(){
        return new JwtAuthenticationSuccessHandler(refreshTokenService, jwtTokenUtil);
    }

    @Bean
    public AuthenticationFailureHandler jwtAuthenticationFailureHandler(){
        return new JwtAuthenticationFailureHandler();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://127.0.0.1:5173");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
