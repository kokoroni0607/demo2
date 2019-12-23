## Spring Security
[简介](#简介)
### 简介
Spring security 是一个强大的和高度可定制的身份验证和访问控制框架。它是确保基于Spring的应用程序的标准。

Spring Security 为基于javaEE的企业应用程序提供一个全面的解决方案。正如你将从这个参考指南发现的，我们试图为你提供一个有用的并且高度可配置的安全系统。

这个简介使用的demo并没有使用oauth2，demo地址[https://github.com/kokoroni0607/demo2.git](https://github.com/kokoroni0607/demo2.git)

### 准备工作

- Spring Boot 基本知识，至少要懂得Controller、RestController、Autowired等这些基本注释。
- lombok的使用，主要用于实体
- MySql 和 mybatis-plus的使用，主要用于

### 程序逻辑
- 使用admin账户登录，可以访问指定接口
- 使用test账户登录，缺少指定角色，接口访问被拒绝
- 用户、角色和资源关系可以使用数据库配置

### 创建demo
最方便的创建spring boot项目当然是使用intellij idea的spring initializr，创建之前还可以选择所需的依赖，比如lombok和我们需要的spring security。或者你可以创建一个空的spring boot项目，然后手动在pom文件里添加，没有本质的区别。
这里贴一下pom：
主体部分：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.2.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.credit</groupId>
    <artifactId>demo2</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>demo2</name>
    <description>Demo project for Spring Boot</description>
```
版本配置：
```xml
    <properties>
        <java.version>1.8</java.version>
        <commons-lang3.version>3.9</commons-lang3.version>
        <mybatis-plus.version>3.2.0</mybatis-plus.version>
        <freemarker.version>2.3.29</freemarker.version>
        <fastdfs-client.version>1.26.7</fastdfs-client.version>
        <oracle.ojdbc6.version>11.2.0.3</oracle.ojdbc6.version>
    </properties>
```
依赖配置：
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!--lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!-- mybatis and mysql-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>
        <dependency>
            <groupId>org.freemarker</groupId>
            <artifactId>freemarker</artifactId>
            <version>${freemarker.version}</version>
        </dependency>
        <!-- datasource-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>com.oracle</groupId>
            <artifactId>ojdbc6</artifactId>
            <version>${oracle.ojdbc6.version}</version>
        </dependency>
        <!-- google-->
        <dependency>
            <groupId>com.vaadin.external.google</groupId>
            <artifactId>android-json</artifactId>
            <version>0.0.20131108.vaadin1</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>com.github.ben-manes.caffeine</groupId>
            <artifactId>guava</artifactId>
        </dependency>
        <!--commons-->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>${commons-lang3.version}</version>
        </dependency>
```
不过也没有验证哪些依赖没有用上，建议都帖进去。

### 数据库和实体设计

需求要求用户是可以有多个角色，每个角色也可以有同一个资源的权限。所以除了用户、角色和资源表之外还需要用户角色关联表和角色资源关联表。这两张表都是一对多的关系，即一个用户可以有多个角色，一个角色可以有多个资源。
贴一下表结构sql：
```sql
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80018
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2019-12-17 16:20:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for realm
-- ----------------------------
DROP TABLE IF EXISTS `realm`;
CREATE TABLE `realm` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `realmname` varchar(255) NOT NULL,
  `resource` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for role_realm
-- ----------------------------
DROP TABLE IF EXISTS `role_realm`;
CREATE TABLE `role_realm` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleid` int(11) NOT NULL,
  `realmid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `roleid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```
这可以算得上是最小化的问题模型了。
但是并不需要把关联表作为实体，只需要把用户account、角色role和权限realm（资源）作为实体类就可以了。其他的事情都可以通过sql来完成。

### 项目的分层和构建
#### mybatis-plus生成
如果你接触过mybatis-plus的代码生成器，那么可以直接使用已有的数据库生成实体类、mapper xml、service层到controller层。这不是本文的重点，可以参考demo代码中的CodeGenerator类，或者mybatis-plus官网：[代码生成器](https://mp.baomidou.com/guide/generator.html)

#### 手动构建：
如果不熟悉mybatis-plus，或者直接使用mybatis，也可以手动构架。这里以用户Account为例：

##### 实体类：
```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

}
```
需要注意的是这里的注解是由mybatis-plus生成的，具体意义可以自行查询，也可以手动写get set等等。

##### mapper：
```java
@Mapper
@Repository
public interface AccountMapper extends BaseMapper<Account> {
}
```
这里的@Repository感觉不加也可以，但是intellij idea总是会在注入的时候报红，强迫症令我加个注解。

##### mapper.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.credit.demo2.mapper.AccountMapper">

    <!-- 开启二级缓存 -->
<!--    <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>-->

    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="com.credit.demo2.entity.Account">
        <id column="id" property="id" />
        <result column="username" property="username" />
        <result column="password" property="password" />
    </resultMap>

    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        id, username, password
    </sql>
</mapper>
```
二级缓存被我注掉了，主要是用不上。

##### Service和ServiceImpl:
```java
public interface AccountService extends IService<Account> {
}

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

}
```

##### controller：
```java
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/test")
    public CommonResult test(){
        return CommonResult.success("test succeed");
    }
}
```
这里我们使用RestController，写个测试接口试试。这里的CommonResult是一个通用返回对象类，返回后会被RestController处理为json然后response。

到这里，把account，role和realm创建完成之后，理论上就可以运行了。我们做好了这一切的准备，就可以进入spring security的设置了。

### UserDetailService
#### UserDetail
在一切的开始，当然是介绍一下UserDetail。源码给出的注释非常明确：“Provides core user information”，提供用户核心信息:
```
Implementations are not used directly by Spring Security for security purposes. They simply store user information which is later encapsulated into Authentication objects. This allows non-security related user information (such as email addresses, telephone numbers etc) to be stored in a convenient location.
```
出于安全目的，Spring Security不会直接使用实现。 它们只是存储用户信息，这些信息随后被封装到Authentication对象中。 这允许将与安全无关的用户信息（例如电子邮件地址，电话号码等）存储在方便的位置。

也就是说，我们写用户信息也给不了Spring Security，他需要一个UserDetail实现来认证，并作为认证（Authentication）对象的一个部分。
当然我们可以这么做来实现自定义UserDetail，但是这里我们并不需要，我们只需要实现UserDetailService就可以完成这种功能。
#### 实现UserDetailService
首先写一个CustomUserDetailService类实现UserDetailService。它只有一个方法需要实现：
```java
public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

}
```
通过用户名获取用户信息。
但是这实际上是个比较棘手的问题。试想：如果你的系统中用户名并不是唯一标识怎么办？因为在某些情况下你需要把用户分成多表，比如外网用户、管理用户或者其他系统用户之类。在这种情况下，这种办法是不适用的。可能你通过别的办法，比如传电话号码，但是他的逻辑就是要求可以通过唯一的String来获取用户信息，以及权限。
所以内部的代码：
```java
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
```
当然accountMapper.selectOne的地方可以改成通过别的标识来查询用户。但是它要求该方法返回的一定是UserDetail的实现，比如上面的org.springframework.security.core.userdetails.User是security核心提供的一个User。他的构造也非常复杂：
```java
	public User(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {

		if (((username == null) || "".equals(username)) || (password == null)) {
			throw new IllegalArgumentException(
					"Cannot pass null or empty values to constructor");
		}

       // 用户名 唯一标识
		this.username = username;
        // 密码
		this.password = password;
        // 是否允许登录
		this.enabled = enabled;
        // 账户没有过期时为true
		this.accountNonExpired = accountNonExpired;
        // 密码没有过期时为true
		this.credentialsNonExpired = credentialsNonExpired;
        // 账户是否锁定
		this.accountNonLocked = accountNonLocked;
        // 权限列表
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
	}
```
关于authorities，可以看他的源码注释：
```
the authorities that should be granted to the caller if they presented the correct username and password and the user is enabled. Not null.

如果提供正确的用户名和密码并启用了用户，应授予调用者的权限。 不为空。
```
所以在这个方法中，如果用户提供的密码是正确的，则需要将用户的权限列表查出来。这个列表的类型是Set<GrantedAuthority>，所以我们可以把用户的所有role查出来，然后创建GrantedAuthority对象把role填进去，得到GrantedAuthority的集合：

```java
    private List<SimpleGrantedAuthority> getAuthority(Integer id) {
        // 从数据库里查用户角色，合并为一个列表
        List<Role> roles = roleMapper.getRoleByAccount(id);
        return roles.stream().map(a ->
                new SimpleGrantedAuthority(a.getRolename()))
                .collect(Collectors.toList());
    }
```
getRoleByAccount的mapper.xml：
```sql
select r.id id,r.rolename rolename
from role rleft join user_role ur on r.id = ur.roleid
where ur.userid = #{id}
```
### SecurityConfigurer
然后我们需要一个CustomSecurityConfig来继承WebSecurityConfigurerAdapter类。
#### WebSecurityConfigurerAdapter

WebSecurityConfigurerAdapter是一个适配器类：
```
Provides a convenient base class for creating a {@link 
WebSecurityConfigurer}* instance. The implementation allows customization by 
overriding methods.

提供用于创建WebSecurityConfigurer实例的便捷基类。 该实现允许通过覆盖方法进行自定义。
```
继承该配置类可以重写三个方法：
```java

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //codeing here
    }
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //codeing here
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        //codeing here
    }
```

#### configure AuthenticationManagerBuilder
这个configure的源码注释说的很明显了：
```
Used by the default implementation of authenticationManager() to attempt to obtain an AuthenticationManager. If overridden, the AuthenticationManagerBuilder should be used to specify the AuthenticationManager.

由authenticationManager（）的默认实现使用，以尝试获取AuthenticationManager。 如果被覆盖，AuthenticationManagerBuilder应该用于指定AuthenticationManager。
```
参数AuthenticationManagerBuilder用于指定AuthenticationManager。
关于AuthenticationManager可以看[官方文档](https://docs.spring.io/spring-security/site/docs/5.2.2.BUILD-SNAPSHOT/reference/htmlsingle/#core-services-authentication-manager)，或者网翻[中文文档](https://www.springcloud.cc/spring-security-zhcn.html#core-services-authentication-manager)，但是他的翻译水平是真的8行，还是得搭配着原版看。
总之，这里可以配置自己的认证服务authenticationProvider，也可以userDetail以及密码规则。这里把我们写的CustomUserDetailService设置进去：
```java
    //配置全局设置
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //设置userDetail以及密码规则
        auth.userDetailsService(userDetailService).passwordEncoder(new BCryptPasswordEncoder());
    }
```
#### configure HttpSecurity
这里使用HttpSecurity实现具体的权限控制规则配置：
```java
@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                //跨域保护
                csrf().disable()
                // 页面js表单提交需要加上
                // var token = $("meta[name='_csrf']").attr("content");
                // var header = $("meta[name='_csrf_header']").attr("content");
                //  .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //  .and()
                // 1.登录配置
                .formLogin()
                // 请求时未登录跳转接口
                // .loginPage("/login")
                // 用户名参数
                .usernameParameter("username")
                // 密码参数
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
```
注释还算详尽，有错误或问题也可以联系我。
可以看出，我们可以自定义实现登录成功处理器customSuccessHandler、失败处理器customFailureHandler和权限不足异常处理器customAccessDeniedHandler，这将在下一部分中一步步实现。

这些配置大多都有框架约定的默认配置，需要注意的地方是第四部分：鉴权
antMatchers的参数使用正则去匹配请求链路，意味着你可以使用
```java
.antMatchers(" /** ").permitAll()
```
去允许一切请求。但是你会发现这一配置以后会失效，因为permitAll会给没有登录的用户适配一个AnonymousAuthenticationToken，设置到SecurityContextHolder，方便后面的filter可以统一处理authentication。所以本质上还是会通过filter处理，并没有说匹配上了就不处理了。

#### configure WebSecurity
WebSecurity由WebSecurityConfiguration创建，用于创建称为Spring Security Filter Chain（springSecurityFilterChain）的FilterChainProxy。 springSecurityFilterChain是DelegatingFilterProxy委派给的Filter。
所以我们可以添加配置，让filter忽略一些文件请求：
```java
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/templates/**",, "/web/**")
                .antMatchers("/**/*.js", "/**/*.css", "/**/*.map", "/**/*.html",
                        "/**/*.png");
```
那么WebSecurity.ignoring() 和 上面的 HttpSecurity.permitAll()有什么区别呢？

- ingore是完全绕过了spring security的所有filter，相当于不走spring security
- permitall没有绕过spring security，其中包含了登录的以及匿名的。

也就是说，这里配的ignoring真的会被完全忽略。
完成了以上三个configure之后，WebSecurityConfigurerAdapter的实现基本上就完成了。接下来需要实现几个处理器。
### 处理器handler
其实成功处理等等handler类不是必须的，你可能在别的文章里看到了更加简单的配置。这里是为了让程序更具有拓展性，因为企业及程序经常需要附加操作，比如在登录成功以后返回用户菜单等。
#### SuccessHandler
我们写一个类CustomSuccessHandler实现AuthenticationSuccessHandler接口。注意，这个类需要被spring托管，所以需要加@Component注解。
然后实现他的方法：
```java
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("login success");
        redirectStrategy.sendRedirect(request, response, "/index/welcome");
    }

```
可以看到，登录验证成功以后，用户的信息authentication也可以在这个方法中获取。所以我们可以通过以下方式获取用户名信息：
```java
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
```
前面我们提过，UserDetail会存在authentication的Principal中。但是这里有一个问题就是，如果使用sendRedirect重定向，response就无法携带数据。当然我们可以使用拼接把数据接在url里，然而一旦我们需要返回比较复杂的数据比如用户菜单，这种方式就无法满足了。

#### FailureHandler
没什么可说的，和SuccessHandler类似，写一个CustomFailureHandler实现AuthenticationFailureHandler接口，实现他的方法：
```java
@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    }
```
这里可以抛出一个自定义异常，或者跳转到错误页，或者只返回信息响应给前端处理。

#### AccessDeniedHandler
与上面类似，写一个CustomAccessDeniedHandler实现AccessDeniedHandler，实现他的方法：
```java
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    }
```
这里当然也可以抛出一个自定义异常，也可以返回信息响应给前端处理，但是用户的登录信息依然存在，所以并不需要跳转登录页，也不用让用户退出。

完成了这些，简单的配置就结束了，这时候应该可以尝试运行，如果不可以，检查一下哪里的细节出了问题。我们在浏览器里请求: [http://localhost:8084/login](http://localhost:8084/login) 就可以看到框架给的默认登录页面：
![88570b2c18fec0e50c8eef3b485276a4.png](en-resource://database/1350:1)

就可以直接登陆数据库里已存在的账户了。

但是我们的目的不仅于此，我们需要实现动态权限。
### 动态权限实现
#### withObjectPostProcessor
让我们再回到configure HttpSecurity中介绍的.authorizeRequests()。该方法返回一个ExpressionInterceptUrlRegistry对象，它提供了ObjectPostProcessor以让用户实现更多想要的高级配置。可以看他的withObjectPostProcessor()方法使用：
```java
		// 4.鉴权配置
                .authorizeRequests()
                // 其他请求均需要鉴权
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(securityMetadataSource);
                        o.setAccessDecisionManager(accessDecisionManager);
                        return o;
                    }
                })
```

可以看到，这个方法需要两个东西：
- securityMetadataSource 权限资源
- accessDecisionManager 权限决策
然后返回一个继承了FilterSecurityInterceptor类的对象。值得注意的是，作为AbstractSecurityInterceptor的三个实现之一，FilterSecurityInterceptor是.security.web包下的，说明他是web专用的。
那么接下来我们需要实现自己的权限资源和决策器。
#### securityMetadataSource
写一个AppFilterInvocationSecurityMetadataSource实现FilterInvocationSecurityMetadataSource接口。该接口是空的，继承了SecurityMetadataSource接口。需要实现三个方法：
##### supports
supports方法返回类对象是否支持校验，web项目一般使用FilterInvocation来判断，或者直接返回true：
```java
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
```
##### getAllConfigAttributes
getAllConfigAttributes方法如果返回了所有定义的权限资源，Spring Security会在启动时校验每个ConfigAttribute是否配置正确，不需要校验直接返回null。
```java
@Overridepublic Collection<ConfigAttribute> getAllConfigAttributes() {    return null;}
```
##### getAttributes
getAttributes方法返回本次访问需要的权限，可以有多个权限。
```java
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

```
值得注意的是，即便url匹配不到，依然需要用户登录才可以访问，意味着每一个用户都具有role_login权限。如果不需要这样的角色设置，可以直接返回null，拦截器将不会验证用户是否有该角色。

当然不要忘了，同上面的处理器类一样，该类也是需要加@Component注解的。
#### AccessDecisionManager
写一个CustomAccessDecisionManager类实现AccessDecisionManager接口。
同样也有三个方法，其中support方法虽然有两个，但是和上面类似：
```java
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
```
下面重点说一下决策方法decide：
```java
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

```

decide方法的三个参数中：
- authentication包含了当前的用户信息，包括拥有的权限。这里的权限来源就是前面登录时UserDetailsService中设置的authorities。
- object就是FilterInvocation对象，可以得到request等web资源。
- configAttributes是本次访问需要的权限。

需要说明的是requestUrl匹配login和authentication的部分是因为我发现login的路径permitAll失效，他会一直不停的重定向，导致不停的进入这个决策方法。如果你可以处理这个问题或者告诉我为什么，请联系我。
总之，完成这三个部分之后，设置动态权限就大致完成了。
### 测试
为了体现差异，我们需要两个账户admin和test，然后给他们分别设置角色ROLE_ADMIN和ROLE_TEST。然后每个角色设置一个资源：

rolename | realmname | resource
- | :-: | :-: | 
ROLE_ADMIN | /account/admin | /account/admin
ROLE_ADMIN | /account/test | /account/test
ROLE_TEST |  /account/test | /account/test

然后我们使用admin账户登录：
请求admin接口：
{"data":"test succeed","code":200,"message":"成功","success":true}
我们在再用test账户登录后请求接口：
access denied

这样就实现了对admin资源的控制。
