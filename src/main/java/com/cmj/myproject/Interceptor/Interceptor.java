package com.cmj.myproject.Interceptor;

import com.cmj.myproject.domain.Member;
import com.cmj.myproject.dto.MemberResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Security;

@Slf4j
public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Member) {
            Member user = (Member) principal;
            // user 객체를 사용하여 사용자 정보에 접근
            log.info("user : {}", user);
            // ...
        } else {
            log.info("principal : {}", principal);
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