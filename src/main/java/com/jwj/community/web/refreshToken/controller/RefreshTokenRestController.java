package com.jwj.community.web.refreshToken.controller;

import com.jwj.community.config.security.utils.JwtTokenUtil;
import com.jwj.community.domain.entity.Member;
import com.jwj.community.domain.member.service.MemberService;
import com.jwj.community.domain.refreshToken.service.RefreshTokenService;
import com.jwj.community.web.common.result.Result;
import com.jwj.community.web.member.jwt.JwtToken;
import com.jwj.community.web.refreshToken.dto.request.RefreshTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refresh")
public class RefreshTokenRestController {

    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/refresh")
    public ResponseEntity<Result<JwtToken>> refresh(@RequestBody RefreshTokenRequest request){
        String email = jwtTokenUtil.getUsernameFromToken(request.getRefreshToken());
        Member savedMember = memberService.findByEmail(email);
        JwtToken jwtToken = jwtTokenUtil.generateToken(savedMember);

        refreshTokenService.changeRefreshToken(email, jwtToken);

        Result<JwtToken> result = Result.<JwtToken>builder()
                .data(jwtToken)
                .build();

        return new ResponseEntity<>(result, OK);
    }

}
