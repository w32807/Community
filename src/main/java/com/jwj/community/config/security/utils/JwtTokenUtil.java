package com.jwj.community.config.security.utils;

import com.jwj.community.domain.common.enums.Roles;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.web.login.jwt.JwtToken;
import com.jwj.community.web.login.jwt.JwtTokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.io.Encoders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.jwj.community.web.login.jwt.JwtTokenType.ACCESS;
import static com.jwj.community.web.login.jwt.JwtTokenType.REFRESH;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.lang.System.currentTimeMillis;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toSet;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class JwtTokenUtil {

    private final String SECRET = Encoders.BASE64.encode("47edd4a2-8555-4078-9b53-b652e11fc5dd".getBytes());
    private final long AT_EXP_TIME = 60 * 60 * 24 * 7;
    private final long RT_EXP_TIME = 60 * 60 * 24 * 30 * 3;

    /**
     * username 으로 토큰생성
     * @param member
     * @return
     */
    public JwtToken generateToken(Member member){
        return JwtToken.builder()
                .accessToken(doGenerateToken(member, ACCESS))
                .refreshToken(doGenerateToken(member, REFRESH))
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

    private String doGenerateToken(Member member, JwtTokenType jwtTokenType) {
        if(member == null || isEmpty(member.getEmail())){
            return null;
        }

        if(jwtTokenType == null){
            return null;
        }

        return Jwts.builder()
                .setHeader(getDefaultHeader())
                // payload로써 토큰에 담을 정보들
                .setClaims(getDefaultClaims(member))
                // 토큰 제목??
                .setSubject(member.getEmail())
                // 토큰이 발급 된 시간
                .setIssuedAt(new Date(currentTimeMillis()))
                // 토큰이 만료될 시간
                .setExpiration(new Date(currentTimeMillis() + getExpTimeByJwtType(jwtTokenType)))
                // 서명
                .signWith(hmacShaKeyFor(SECRET.getBytes(UTF_8)), HS256)
                .compact();
    }

    /**
     * secret 키를 가지고 토큰에서 정보 검색
     * @param token
     * @return
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET.getBytes()).build().parseClaimsJws(token).getBody();
    }

    /**
     * 토큰 만료 체크
     * @param token
     * @return
     */
    private Boolean isExpiredToken(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
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

    private long getExpTimeByJwtType(JwtTokenType jwtTokenType){
        return jwtTokenType == ACCESS ? AT_EXP_TIME : RT_EXP_TIME;
    }
}
