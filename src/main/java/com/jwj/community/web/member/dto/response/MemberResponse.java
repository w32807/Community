package com.jwj.community.web.member.dto.response;

import com.jwj.community.domain.common.enums.Level;
import com.jwj.community.domain.common.enums.MemberState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberResponse {

    private Long id;
    private String email;
    private String nickname;
    private int levelPoint;
    private Level level;
    private MemberState state;

}
