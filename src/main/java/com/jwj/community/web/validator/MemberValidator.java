package com.jwj.community.web.validator;

import com.jwj.community.domain.member.service.MemberService;
import com.jwj.community.web.member.dto.request.MemberSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor
public class MemberValidator implements Validator {

    private final MemberService memberService;

    @Override
    public boolean supports(Class<?> clazz) {
        return (MemberSaveRequest.class.isAssignableFrom(clazz));
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberSaveRequest dto = (MemberSaveRequest) target;
        isEmailDuplicate(dto.getEmail(), errors);
        isNickDuplicate(dto.getNickname(), errors);
        isEqualPassword(dto.getPassword(), dto.getConfirmPassword(), errors);
    }

    private void isEmailDuplicate(String email, Errors errors) {
        if(memberService.findByEmail(email) != null){
            errors.reject("emailDuplicated", "duplicate.email");
            errors.rejectValue("email", "","이미 사용중인 이메일입니다.");
        }
    }

    private void isNickDuplicate(String nickname, Errors errors) {
        if(memberService.findByNickname(nickname) != null){
            errors.rejectValue("nickname", "","이미 사용중인 닉네임입니다.");
        }
    }

    private Errors isEqualPassword(String pwd, String conPwd, Errors errors) {
        if(hasText(pwd) && hasText(conPwd) && !pwd.equals(conPwd)){
            errors.rejectValue("password", "","비밀번호가 일치하지 않습니다.");
        }
        return errors;
    }

}
