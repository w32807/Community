package com.jwj.community.web.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginMemberDTO {

    private final String email;

}
