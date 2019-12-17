package com.credit.demo2.security;

import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 *
 * @author weiming.zhu
 * @date 2019/12/12 13:12
 */
@Log
@Component
public class CustomFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("login failure");
        // 这里虽然不推荐使用"application/json;charset=UTF-8"，但是好像没有什么好替代的
        // 他的注释也写的很牵强，又不是说所有浏览器都做了处理
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write("failed");
    }
}
