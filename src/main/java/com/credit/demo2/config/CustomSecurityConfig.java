package com.credit.demo2.config;

import com.credit.demo2.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author weiming.zhu
 * @date 2019/12/17 13:31
 */
// 开启WebSecurity
@EnableWebSecurity
// 开启注解
@EnableGlobalMethodSecurity(prePostEnabled = true)
// 加载顺序
@Order(-1)
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailService userDetailService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomFailureHandler customFailureHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAccessDecisionManager customAccessDecisionManager;
    private final AppFilterInvocationSecurityMetadataSource metadataSource;

    public CustomSecurityConfig(CustomUserDetailService userDetailService, CustomSuccessHandler customSuccessHandler, CustomFailureHandler customFailureHandler, CustomAccessDeniedHandler customAccessDeniedHandler, CustomAccessDecisionManager customAccessDecisionManager, AppFilterInvocationSecurityMetadataSource metadataSource) {
        this.userDetailService = userDetailService;
        this.customSuccessHandler = customSuccessHandler;
        this.customFailureHandler = customFailureHandler;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAccessDecisionManager = customAccessDecisionManager;
        this.metadataSource = metadataSource;
    }

    //配置全局设置
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //设置userDetail以及密码规则
        auth.userDetailsService(userDetailService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 需要释放的路径
     */
    private static final String[] ENDPOINTS = {
            "/actuator/**",
            "/*/v2/api-docs",
            "/swagger/api-docs",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/druid/**"
    };


    /**
     * 需要释放的静态资源
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/templates/**", "/sanm/**", "/web/**", "/admin/**")
                .antMatchers("/**/*.js", "/**/*.css", "/**/*.map", "/**/*.html",
                        "/**/*.png");
    }

    /**
     * 核心配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable()
                //跨域保护
                // 页面js表单提交需要加上
                // var token = $("meta[name='_csrf']").attr("content");
                // var header = $("meta[name='_csrf_header']").attr("content");
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                // 1.登录配置
                .formLogin()
                // 请求时未登录跳转接口
//                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                // 指定自定义form表单请求的路径
                .loginProcessingUrl("/authentication")
                // 登陆成功处理器
                .successHandler(customSuccessHandler)
                // 失败处理器
                .failureHandler(customFailureHandler)
                // 允许通过
                .permitAll()
                .and()
                // 2.登出配置
                .logout()
                // 路径
                .logoutUrl("/logout")
                // 删除cookie
                .deleteCookies("JSESSIONID")
                // 登出成功后重定向路径
                .logoutSuccessUrl("/login")
                // 全部允许
                .permitAll()
                .and()
                // 3.解决X-Frame-Options deny
                .headers().frameOptions().disable()
                .and()
                // 4.鉴权配置
                .authorizeRequests()
                // 其他请求均需要鉴权
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(metadataSource);
                        o.setAccessDecisionManager(customAccessDecisionManager);
                        return o;
                    }
                })
                // 登录全部通过
                .antMatchers("/login").permitAll()
                // 指定资源全部通过
                .antMatchers(ENDPOINTS).permitAll()
                .and()
                // 异常处理
                .exceptionHandling()
                //权限不足异常处理器
                .accessDeniedHandler(customAccessDeniedHandler);
    }
}
