package com.jwj.community.web.member.dto.request;

import lombok.Data;

@Data
public class MemberUpdateRequest {

    private Long id;

    private String password;

    private String passwordConfirm;

}
