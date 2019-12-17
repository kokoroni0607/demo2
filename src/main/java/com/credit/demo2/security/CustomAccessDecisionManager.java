package com.credit.demo2.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * 自定义链路决策器
 * @author weiming.zhu
 * @date 2019/12/13 15:19
 */
@Component
public class CustomAccessDecisionManager  implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        //获取请求的url
        String requestUrl = ((FilterInvocation) object).getRequestUrl();

        Iterator<ConfigAttribute> iterator = configAttributes.iterator();
        while (iterator.hasNext()){
            ConfigAttribute configAttribute = iterator.next();
            //访问该请求url需要的权限
            String needRole = configAttribute.getAttribute();
            //如果只是登陆就能访问，即没有匹配到资源信息
            if ("ROLE_LOGIN".equals(needRole)){
                if(requestUrl.contains("login")|| requestUrl.contains("authentication")){
                    return;
                }
                //判断是否登陆，没有登陆则authentication是AnonymousAuthenticationToken接口实现类的对象
                if (authentication instanceof AnonymousAuthenticationToken){
                    throw new BadCredentialsException("未登录");
                } else return;
            }
            //如果匹配上了资源信息，就拿登陆用户的权限信息来对比是否存在于已匹配的角色集合中
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals(needRole)){
                    return;
                }
            }
        }
        //如果没有匹配上，则权限不足
        throw new AccessDeniedException("权限不足");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
