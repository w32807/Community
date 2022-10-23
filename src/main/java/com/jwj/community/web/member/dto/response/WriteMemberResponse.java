package com.jwj.community.web.member.dto.response;

import com.jwj.community.domain.entity.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WriteMemberResponse {

    private String email;
    private String nickname;

    public static WriteMemberResponse of(Member member){
        return WriteMemberResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

}
