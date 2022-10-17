package com.jwj.community.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwj.community.config.security.config.LoginContext;
import com.jwj.community.config.security.token.JwtAuthenticationToken;
import com.jwj.community.config.security.utils.JwtTokenUtil;
import com.jwj.community.web.exception.dto.ErrorResult;
import com.jwj.community.web.exception.exceptions.jwt.AccessTokenNotFound;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jwj.community.web.member.jwt.JwtConst.AUTHORIZATION;
import static com.jwj.community.web.member.jwt.JwtConst.TOKEN_HEADER_PREFIX;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Locale.getDefault;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

/**
 * JWT 로그인 요청이 아닌 다른 매 요청마다 JWT 토큰이 유효한지 체크하는 필터
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final MessageSource messageSource;
    private final JwtTokenUtil jwtTokenUtil;

    final String[] whitelist = {"/api/login", "/api/refresh/refresh"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String authorizationHeader = String.valueOf(request.getHeader(AUTHORIZATION));

        if (!isRequiredAuthPath(requestURI)) {
            filterChain.doFilter(request, response);
        }else if (!isValidAuthHeader(authorizationHeader)){
            handleInvalidToken(response);
        }else{
            try{
                // 토큰에서 username을 얻어와서 SecurityContextHolder에 저장 ("bearer 6자리와 띄어쓰기 1자리를 포함하여 자른다)
                String accessToken = authorizationHeader;

                if(!jwtTokenUtil.isExpiredToken(accessToken)){
                    LoginContext loginContext = (LoginContext) userDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(accessToken));
                    // authenticationToken 생성 시 권한정보까지 전달하여 생성해야 시큐리티에서 인증된 사용자로 판단한다.
                    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(loginContext.getUsername(), loginContext.getPassword(), loginContext.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request, response);
                }
            }catch(ExpiredJwtException e){
                handleExpiredJwtException(response, e);
            }catch(SignatureException e){
                handleSignatureException(response, e);
            }
        }
    }

    private boolean isRequiredAuthPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

    private boolean isValidAuthHeader(String authorizationHeader){
        return hasText(authorizationHeader)
                && authorizationHeader.startsWith(TOKEN_HEADER_PREFIX)
                && !"null".equals(authorizationHeader);
    }

    private void handleExpiredJwtException(HttpServletResponse response, ExpiredJwtException e) throws IOException {
        String errorMessage = messageSource.getMessage("error.expiredJwtToken", null, getDefault());
        setErrorResponse(response, errorMessage, e);
    }

    private void handleSignatureException(HttpServletResponse response, SignatureException e) throws IOException {
        String errorMessage = messageSource.getMessage("error.invalidJwtSignature", null, getDefault());
        setErrorResponse(response, errorMessage, e);
    }

    private void handleInvalidToken(HttpServletResponse response) throws IOException {
        String errorMessage = messageSource.getMessage("error.noJwtToken", null, getDefault());
        setErrorResponse(response, errorMessage, new AccessTokenNotFound());
    }

    private void setErrorResponse(HttpServletResponse response, String errorMessage, Exception ex) throws IOException {
        log.error(errorMessage);

        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());

        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(String.valueOf(UNAUTHORIZED.value()))
                .errorMessage(errorMessage)
                .exception(ex)
                .build();

        new ObjectMapper().writeValue(response.getWriter(), errorResult);
    }
}
