package com.cmj.myproject.Interceptor;

import com.cmj.myproject.config.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        log.info("principal : {}", principal);



        if (!Objects.equals(username, "anonymousUser")) {
            if (request.getRequestURI().contains("/login") || request.getRequestURI().contains("/signup")) {
                response.sendRedirect("/");
                return false;
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}