package com.innilabs.restboard.config;



import com.innilabs.restboard.auth.JwtAuthenticationFilter;
import com.innilabs.restboard.auth.JwtProvider;

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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    //private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtProvider tokenProvider;
    @Override
    public void configure(WebSecurity web) throws Exception
    {// static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
        web.ignoring().antMatchers("/css/**","/js/**","/img/**","lib/**",
                                    "/v2/api-docs", 
                                    "/swagger-resources/**", 
                                    "/swagger-ui.html", 
                                    "/webjars/**", 
                                    "/swagger/**",
                                    "/configuration/ui",
                                    "/configuration/security");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http
                .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리.
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증할것이므로 세션필요없으므로 생성안함.
            .and()    
                .httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .formLogin().disable()
                //.loginProcessingUrl() //loadUserByUsername 접근함
                .authorizeRequests()
                // 페이지 권한 설정
                .antMatchers("/users/signin","/posts").permitAll()
                .antMatchers("/posts/create").hasRole("MEMBER") //자동으로 앞에 "ROLE_"이 삽입 

            .and()                                              
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
}