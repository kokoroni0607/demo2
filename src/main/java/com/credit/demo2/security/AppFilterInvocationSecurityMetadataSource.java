package com.credit.demo2.security;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.credit.demo2.entity.Realm;
import com.credit.demo2.entity.Role;
import com.credit.demo2.mapper.RealmMapper;
import com.credit.demo2.mapper.RoleMapper;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 自定义 MetadataSource
 *
 * @author weiming.zhu
 * @date 2019/12/13 14:40
 */
@Component
public class AppFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final RealmMapper realmMapper;
    private final RoleMapper roleMapper;

    public AppFilterInvocationSecurityMetadataSource(RealmMapper realmMapper, RoleMapper roleMapper) {
        this.realmMapper = realmMapper;
        this.roleMapper = roleMapper;

    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取请求的url
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        // 查询所有受限资源
        List<Realm> realmList = realmMapper.selectList(Wrappers.lambdaQuery());
        // 遍历资源，匹配url
        for (Realm a : realmList) {
            // 匹配到了，再查询有该资源的角色
            if (requestUrl.contains(a.getResource())) {
                // 将含有该资源的角色查出来
                List<Role> roles = roleMapper.getRoleByRealmId(a.getId());
                // 转成角色名数组
                String[] rolenames = roles.stream().map(Role::getRolename).toArray(String[]::new);
                // 写入SecurityConfig
                return SecurityConfig.createList(rolenames);
            }
        }
        // 匹配不到则不是受限资源，说明登录即可访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
