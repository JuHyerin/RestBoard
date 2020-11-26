package com.innilabs.restboard.config;

import com.innilabs.restboard.service.AccountService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@EnableGlobalMethodSecurity(
    prePostEnabled = true, 
    securedEnabled = true, 
    jsr250Enabled = true)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //private final AccountService accountService;

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        // static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
        web.ignoring().antMatchers("/css/**","/js/**","/img/**","lib/**",
        "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**",
        "/configuration/ui","/configuration/security");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .cors().disable()
                .httpBasic().disable()
            .authorizeRequests()
                // 페이지 권한 설정
                .antMatchers("/login","/users","/posts").permitAll()
                .antMatchers("/**").hasRole("MEMBER") //자동으로 앞에 "ROLE_"이 삽입 
                                                          //-> DB Table: "ROLE_권한명" 형식으로 삽입
            .and() // 로그인 설정
                .formLogin()
                //.loginPage("/users/login")
                .defaultSuccessUrl("/")
                //.permitAll()
            .and() // 로그아웃 설정
                .logout()
                //.logoutRequestMatcher(new AntPathRequestMatcher("/v1/users/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                //.permitAll()
            .and()
                // 403 예외처리 핸들링
                .exceptionHandling().accessDeniedPage("/users/denied");
    }

    /* @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 사용자 세부 서비스를 설정하기 위한 오버라이딩이다.
        auth
            .userDetailsService(accountService) // 로그인 관리
            .passwordEncoder(passwordEncoder());
    } */

    

    // passwordEncoder() 구현
    // 암호를 해시시키는 경우 BCryptPasswordEncoder를 사용한다. 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}