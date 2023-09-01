package com.cmj.myproject.config.security;

import com.cmj.myproject.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info(">>>>>> 로그인 성공 <<<<<<");
        setDefaultTargetUrl("/");

        Member m = authentication.getPrincipal() instanceof MemberAdapter ? ((MemberAdapter) authentication.getPrincipal()).getMember() : MemberAdapter.createAnonymousMember();

        request.getSession().setAttribute("m", m);

        super.onAuthenticationSuccess(request, response, authentication);


    }

}