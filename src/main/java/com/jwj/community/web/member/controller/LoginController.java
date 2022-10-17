package com.jwj.community.web.member.controller;

import com.jwj.community.domain.entity.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/loginForm")
    public String loginForm(){
        return "/login/loginForm";
    }

    /**
     * form & PostMapping을 사용한 로그아웃은 Spring Security에서 처리할 수 있지만
     * a태그 & GetMapping을 사용한 로그아웃은 개발자가 직접 SecurityContext를 초기화 해야 한다.
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login/loginForm";
    }

    /**
     * form & PostMapping을 사용한 로그아웃은 Spring Security에서 처리할 수 있지만
     * a태그 & GetMapping을 사용한 로그아웃은 개발자가 직접 SecurityContext를 초기화 해야 한다.
     * @return
     */
    @GetMapping("/loginFail")
    public String loginFail(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "exception", required = false) String exception, RedirectAttributes redirectAttributes){

        redirectAttributes.addFlashAttribute("error", error);
        redirectAttributes.addFlashAttribute("exception", exception);

        // 이후 화면에서 div가 error=true일 때 보여지도록 처리하기
        return "redirect:/login/loginForm";
    }

    @GetMapping("/denied")
    public String accessDenied(@RequestParam(value = "exception", required = false) String exception, RedirectAttributes redirectAttributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        redirectAttributes.addAttribute("member", member.toResponse());
        redirectAttributes.addAttribute("exception", exception);

        return "redirect:/login/loginForm";
    }

}
