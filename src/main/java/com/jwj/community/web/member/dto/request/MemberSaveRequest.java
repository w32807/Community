package com.jwj.community.web.member.dto.request;

import com.jwj.community.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSaveRequest {

    @Email(message = "{field.required.emailFormat}")
    @NotBlank(message = "{field.required.email}")
    private String email;

    @NotBlank(message = "{field.required.password}")
    private String password;

    @NotBlank(message = "{field.required.confirmPassword}")
    private String confirmPassword;

    @NotBlank(message = "{field.required.nickname}")
    private String nickname;

    public Member toEntity(){
        return new Member(this);
    }

}
