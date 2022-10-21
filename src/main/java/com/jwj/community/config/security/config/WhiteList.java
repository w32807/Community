package com.jwj.community.config.security.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class WhiteList {

    private final String[] whiteList = {
        "/api/login", "/api/refresh/**", "/api/member/addMember"
    };

}
