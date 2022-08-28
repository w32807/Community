package com.jwj.community.config.security.handler;

import com.jwj.community.domain.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.jwj.community.web.common.constant.SessionConst.LOGIN_USER;

@Slf4j
@Component
public class FormLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	// 인증 전 사용자가 요청했던 페이지가 있는 경우, 인증 후 요청했던 페이지로 이동하기 위해 선언
	private RequestCache requestCache = new HttpSessionRequestCache();
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("LoginSuccessHandler 실행");

		HttpSession session = request.getSession(); // false 값을 주지 않아 세션이 없다면 생성하면서 가져오기
		session.setAttribute(LOGIN_USER, getLoginMember(authentication));

		setDefaultTargetUrl("/main/home");
		redirectStrategy.sendRedirect(request, response, getRedirectUrl(request, response));
	}
	
	private String getRedirectUrl(HttpServletRequest request, HttpServletResponse response){
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		return savedRequest == null ? getDefaultTargetUrl() : savedRequest.getRedirectUrl();
	}

	private Member getLoginMember(Authentication authentication) {
		return (Member) authentication.getPrincipal();
	}

}
