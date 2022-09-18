package com.jwj.community.config.security.utils;

import com.jwj.community.domain.common.enums.Roles;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.web.login.jwt.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.io.Encoders.BASE64;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.lang.System.currentTimeMillis;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toSet;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;
    private final long DEFAULT_EXP_TIME = currentTimeMillis();
    private final long AT_EXP_TIME = 60 * 60 * 24 * 7;
    private final long RT_EXP_TIME = 60 * 60 * 24 * 30 * 3;

    /**
     * username 으로 토큰생성
     * @param member
     * @return
     */
    public JwtToken generateToken(Member member){
        return JwtToken.builder()
                .accessToken(doGenerateAccessToken(member))
                .refreshToken(doGenerateRefreshToken(member))
                .build();
    }

    /**
     * jwt 토큰에서 username 조회
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * jwt 토큰에서 권한조회 검색
     * 토큰을 다시 claims로 변환하면 String 값으로 변환되기 때문에
     * 다시 Set으로 만들어주어야 한다.
     * @param token
     * @return
     */
    public Set<Roles> getRolesFromToken(String token){
        Claims claims = getAllClaimsFromToken(token);
        List<String> roles = claims.get("roles", List.class);
        return roles.stream().map(role -> Roles.findRole(role)).collect(toSet());
    }

    /**
     * jwt 토큰에서 날짜 만료 검색
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 토큰 검증
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean isValidToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return(username.equals(userDetails.getUsername()) && !isExpiredToken(token));
    }

    /**
     * 토큰 만료 체크
     * @param token
     * @return
     */
    public Boolean isExpiredToken(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * AccessToken은 사용자 정보를 담는다.
     * @param member
     * @return
     */
    private String doGenerateAccessToken(Member member) {
        if(member == null || isEmpty(member.getEmail())){
            return null;
        }

        return Jwts.builder()
                .setHeader(getDefaultHeader())
                // payload로써 토큰에 담을 정보들
                .setClaims(getDefaultClaims(member))
                // 토큰 제목??
                .setSubject(member.getEmail())
                // 토큰이 발급 된 시간
                .setIssuedAt(new Date(DEFAULT_EXP_TIME))
                // 토큰이 만료될 시간
                .setExpiration(new Date(DEFAULT_EXP_TIME + AT_EXP_TIME))
                // 서명
                .signWith(hmacShaKeyFor(encodedSecretKey(SECRET_KEY).getBytes(UTF_8)), HS256)
                .compact();
    }

    /**
     * RefreshToken은 만료시간 정보만 담는다.
     * @param member
     * @return
     */
    private String doGenerateRefreshToken(Member member) {
        if(member == null || isEmpty(member.getEmail())){
            return null;
        }

        return Jwts.builder()
                // 토큰이 만료될 시간
                .setExpiration(new Date(DEFAULT_EXP_TIME + RT_EXP_TIME))
                // 서명
                .signWith(hmacShaKeyFor(encodedSecretKey(SECRET_KEY).getBytes(UTF_8)), HS256)
                .compact();
    }

    /**
     * secret 키를 가지고 토큰에서 정보 검색
     * @param token
     * @return
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(encodedSecretKey(SECRET_KEY).getBytes()).build().parseClaimsJws(token).getBody();
    }

    private Map<String, Object> getDefaultHeader(){
        Header header = new DefaultHeader();
        header.put("typ", "JWT");
        return (Map<String, Object>) header;
    }

    private Map<String, Object> getDefaultClaims(Member member){
        Claims claims = new DefaultClaims();

        claims.put("email", member.getEmail());
        claims.put("roles", member.getRoleSet());

        return claims;
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private String encodedSecretKey(String secretKey){
        return BASE64.encode(SECRET_KEY.getBytes());
    }

}
