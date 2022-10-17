package com.jwj.community.web.member.dto.request;

import com.jwj.community.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSaveRequest {

    @Email(message = "이메일 형식을 확인해 주세요.")
    @NotEmpty(message = "이메일은 필수 입력입니다.")
    private String email;
    
    @NotEmpty(message = "비밀번호는 필수 입력입니다.")
    private String password;
    
    @NotEmpty(message = "비밀번호 확인은 필수 입력입니다.")
    private String confirmPassword;
    
    @NotEmpty(message = "닉네임은 필수 입력입니다.")
    private String nickname;

    public Member toEntity(){
        return new Member(this);
    }

}
