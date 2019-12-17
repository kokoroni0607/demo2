package com.credit.demo2.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.credit.demo2.entity.Account;
import com.credit.demo2.entity.Role;
import com.credit.demo2.enums.DeleteEnum;
import com.credit.demo2.enums.LoginEnum;
import com.credit.demo2.mapper.AccountMapper;
import com.credit.demo2.mapper.RoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author weiming.zhu
 * @date 2019/12/17 13:11
 */
@Slf4j
@Component
public class CustomUserDetailService implements UserDetailsService {
    private final AccountMapper accountMapper;
    private final RoleMapper roleMapper;

    public CustomUserDetailService(AccountMapper accountMapper, RoleMapper roleMapper) {
        this.accountMapper = accountMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名获取用户
        Account account = accountMapper.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getUsername, username));
        // 验证
        Optional.ofNullable(account).orElseThrow(() -> new UsernameNotFoundException("【用户登录】: This user is not exist:" + username));
        // 注意：这里的user是org.springframework.security.core.userdetails包下
        // spring security提供的实现了UserDetails的实体类
        // 他的构造比较复杂，但是十分全面，可以看他的源码注释
        return new User(account.getUsername(),
                new BCryptPasswordEncoder().encode(account.getPassword()),
                isEnabled(0),
                true,
                true,
                isAccountNonLocked(0),
                getAuthority(account.getId()));
    }

    // 是否禁用
    private boolean isAccountNonLocked(Integer enableStatus) {
        return LoginEnum.ENABLE.getCode().equals(enableStatus);
    }

    // 是否删除
    private boolean isEnabled(Integer deleteFlag) {
        return DeleteEnum.EXIST.getCode().equals(deleteFlag);
    }

    private List<SimpleGrantedAuthority> getAuthority(Integer id) {
        // 从数据库里查用户角色，合并为一个列表
        List<Role> roles = roleMapper.getRoleByAccount(id);
        return roles.stream().map(a ->
                new SimpleGrantedAuthority(a.getRolename()))
                .collect(Collectors.toList());
    }
}
