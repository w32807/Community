package com.jwj.community.web.argumentResolver;

import com.jwj.community.config.security.token.JwtAuthenticationToken;
import com.jwj.community.web.annotation.LoginMember;
import com.jwj.community.web.member.dto.LoginMemberDTO;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 컨트롤러의 파라미터를 체크하는데 내부적으로 캐시가 동작하여 한 번 체크하면 다음부터는 캐시에 저장된 값을 사용함
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(LoginMember.class);
        boolean hasLoginUserType = LoginMemberDTO.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasLoginUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginMemberDTO loginMemberDTO = LoginMemberDTO.builder()
                .email(String.valueOf(authenticationToken.getPrincipal()))
                .build();

        return loginMemberDTO;
    }

}
