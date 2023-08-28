package com.cmj.myproject.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(!Objects.equals(username, "anonymousUser")) {
            if(request.getRequestURI().contains("/login") || request.getRequestURI().contains("/signup")) {
                response.sendRedirect("/");
                return false;
            }
        }


        return HandlerInterceptor.super.preHandle(request, response, handler);


    }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        log.debug("==================== END ======================");
//        log.debug("===============================================");
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    }

}