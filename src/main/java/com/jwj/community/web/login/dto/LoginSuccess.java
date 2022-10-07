package com.jwj.community.web.login.dto;

import com.jwj.community.web.login.jwt.JwtToken;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginSuccess {

    private JwtToken token;
    private String email;
    private String nickName;

}
