package com.xxxx.admin.config.security;

import com.xxxx.admin.config.ClassPathTldsLoader;
import com.xxxx.admin.config.security.component.CaptchaCodeFilter;
import com.xxxx.admin.config.security.component.JxcAuthenticationFailedHandler;
import com.xxxx.admin.config.security.component.JxcAuthenticationSuccessHandler;
import com.xxxx.admin.config.security.component.JxcLogoutSuccessHandler;
import com.xxxx.admin.pojo.User;
import com.xxxx.admin.service.IRbacService;
import com.xxxx.admin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @EnableGlobalMethodSecurity(prePostEnabled = true) 启动security环境
 * 302是有缓存
 */
@SpringBootConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private JxcAuthenticationSuccessHandler jxcAuthenticationSuccessHandler;
    @Autowired
    private JxcAuthenticationFailedHandler jxcAuthenticationFailedHandler;
    @Autowired
    private JxcLogoutSuccessHandler jxcLogoutSuccessHandler;
    @Autowired
    private IUserService userService;
    @Autowired
    private CaptchaCodeFilter captchaCodeFilter;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private IRbacService rbacService;

    /**
     * 放行静态资源
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**",
                "/error/**",
                "/images/**",
                "/js/**",
                "/lib/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                //验证码过滤器
                .addFilterBefore(captchaCodeFilter, UsernamePasswordAuthenticationFilter.class)
                //允许iframe 页面嵌套
                .headers().frameOptions().disable()
                .and()

                .formLogin()
                .usernameParameter("userName")
                .passwordParameter("password")
                .loginPage("/index")
                .loginProcessingUrl("/login")
                .successHandler(jxcAuthenticationSuccessHandler)
                .failureHandler(jxcAuthenticationFailedHandler)
                .and()

                .logout()
                .logoutUrl("/signout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(jxcLogoutSuccessHandler)
                .and()

                .rememberMe()
                .rememberMeParameter("rememberMe")
                .tokenValiditySeconds(7 * 24 * 60 * 60)
                .tokenRepository(persistentTokenRepository())
                .and()

                .authorizeRequests()
                .antMatchers("/index", "/login", "/image").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    /**
     * 重新loadUserByUsername,交个权限框架处理，这是一个单例的bean
     *
     * @return
     */
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User userDetails = userService.findUserByUserName(username);
                /**
                 * 1.查询用户对应的角色
                 * 2.用户扮演的角色有那些权限记录
                 */
                List<String> roleNames = rbacService.findRolesByUserName(username);
                List<String> authorities = rbacService.findAuthoritiesByRoleName(roleNames);
                //todo: SpringSecurity要求角色前面必须加ROLE_
                roleNames = roleNames.stream().map(role ->
                        "ROLE_" + role).collect(Collectors.toList());
                authorities.addAll(roleNames);
                userDetails.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", authorities)));

                return userDetails;
            }
        };
    }

    /**
     * 告诉权限框架userDetailService是上面的框架,密码处理是BCryptPasswordEncoder();
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    @ConditionalOnMissingBean
    public ClassPathTldsLoader classPathTldsLoader() {
        return new ClassPathTldsLoader();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
