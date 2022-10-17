package com.jwj.community.web.member.dto;

import com.jwj.community.web.member.jwt.JwtToken;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginSuccess {

    private JwtToken token;
    private String email;
    private String nickName;

}
