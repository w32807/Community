package com.jwj.community.config.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Component
public class FormLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		log.info("onAuthenticationFailure 실행");

		// 반드시 SecurityConfig 에서 /login 아래로 permitAll 권한을 주어야 함
		setDefaultFailureUrl("/login/loginFail?error=true&exception=" + getErrorMessage(exception));

		super.onAuthenticationFailure(request, response, exception);
	}

	private String getErrorMessage(AuthenticationException exception) throws UnsupportedEncodingException {
		String errorMessage = "이메일 혹은 비밀번호를 확인 해 주세요";

		if(exception instanceof UsernameNotFoundException){
			errorMessage = "이메일을 확인 해 주세요";
		}

		if(exception instanceof BadCredentialsException){
			errorMessage = "비밀번호를 확인 해 주세요";
		}

		if(exception instanceof InsufficientAuthenticationException){
			errorMessage = "추가 인증 실패";
		}

		return URLEncoder.encode(errorMessage, "UTF-8");
	}
}
