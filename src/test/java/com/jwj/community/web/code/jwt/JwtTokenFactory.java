package com.jwj.community.web.code.jwt;

import com.jwj.community.config.security.utils.JwtTokenUtil;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.web.login.jwt.JwtToken;
import com.jwj.community.web.login.request.MemberSaveRequest;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.jwj.community.domain.common.enums.Roles.ROLE_ADMIN;
import static com.jwj.community.domain.common.enums.Roles.ROLE_MEMBER;
import static com.jwj.community.web.login.jwt.JwtConst.TOKEN_HEADER_PREFIX;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.io.Encoders.BASE64;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class JwtTokenFactory {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public JwtToken getRequestJwtToken(){
        Member member = MemberSaveRequest.builder()
                .email("admin@google.com")
                .build()
                .toEntity();

        member.addRole(ROLE_ADMIN);
        member.addRole(ROLE_MEMBER);

        JwtToken jwtToken = jwtTokenUtil.generateToken(member);
        jwtToken.setAccessToken(TOKEN_HEADER_PREFIX + " " + jwtToken.getAccessToken());

        return jwtToken;
    }

    public JwtToken getRequestExpiredJwtToken(){
        JwtToken jwtToken = getExpiredJwtToken();
        jwtToken.setAccessToken(TOKEN_HEADER_PREFIX + " " + jwtToken.getAccessToken());

        return jwtToken;
    }

    public JwtToken getJwtToken(){
        Member member = MemberSaveRequest.builder()
                .email("admin@google.com")
                .build()
                .toEntity();

        member.addRole(ROLE_ADMIN);
        member.addRole(ROLE_MEMBER);

        return jwtTokenUtil.generateToken(member);
    }

    public JwtToken getNoEmailJwtToken(){
        Member member = MemberSaveRequest.builder()
                .password("1234")
                .build()
                .toEntity();

        return jwtTokenUtil.generateToken(member);
    }

    public JwtToken getNoAuthJwtToken(){
        Member member = MemberSaveRequest.builder()
                .email("admin@google.com")
                .build()
                .toEntity();

        return jwtTokenUtil.generateToken(member);
    }

    public JwtToken getExpiredJwtToken(){
        Member member = MemberSaveRequest.builder()
                .email("admin@google.com")
                .build()
                .toEntity();

        String expiredAccessToken = Jwts.builder()
                .setSubject(member.getEmail())
                .setIssuedAt(new Date(0))
                .setExpiration(new Date(0))
                .signWith(hmacShaKeyFor(encodedSecretKey(SECRET_KEY).getBytes(UTF_8)), HS256)
                .compact();

        String expiredRefreshToken = Jwts.builder()
                .setExpiration(new Date(0))
                .signWith(hmacShaKeyFor(encodedSecretKey(SECRET_KEY).getBytes(UTF_8)), HS256)
                .compact();

        return JwtToken.builder()
                .accessToken(expiredAccessToken)
                .refreshToken(expiredRefreshToken)
                .build();
    }

    public JwtToken getNoMemberJwtToken(){
        return jwtTokenUtil.generateToken(null);
    }

    private String encodedSecretKey(String secretKey){
        return BASE64.encode(SECRET_KEY.getBytes());
    }

}
