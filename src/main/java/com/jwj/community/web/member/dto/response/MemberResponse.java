package com.jwj.community.web.member.dto.response;

import com.jwj.community.domain.common.enums.Level;
import com.jwj.community.domain.common.enums.MemberState;
import lombok.Data;

@Data
public class MemberResponse {

    private Long id;
    private String email;
    private String password;
    private String nickname;
    private int levelPoint;
    private Level level;
    private MemberState state;

}
