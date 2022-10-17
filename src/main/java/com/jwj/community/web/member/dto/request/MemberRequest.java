package com.jwj.community.web.member.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberRequest {

    private String email;
    private String password;
    private String nickname;

}
