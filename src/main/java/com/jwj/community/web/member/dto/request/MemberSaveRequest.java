package com.jwj.community.web.member.dto.request;

import com.jwj.community.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSaveRequest {

    private String email;
    private String password;
    private String confirmPassword;
    private String nickname;

    public Member toEntity(){
        return new Member(this);
    }

}
