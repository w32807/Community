package com.jwj.community.web.member.dto.response;

import com.jwj.community.domain.common.enums.Level;
import com.jwj.community.domain.common.enums.MemberState;
import com.jwj.community.domain.entity.Member;
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

    public static MemberResponse of(Member member){
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .levelPoint(member.getLevelPoint())
                .level(member.getLevel())
                .state(member.getState())
                .build();
    }
}
