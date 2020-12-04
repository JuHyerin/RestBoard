package com.innilabs.restboard.config;



import com.innilabs.restboard.auth.JwtAuthenticationFilter;
import com.innilabs.restboard.auth.JwtProvider;
import com.innilabs.restboard.auth.MyOAuth2UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    private final MyOAuth2UserService oAuth2UserService;

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
                //.httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증할것이므로 세션필요없으므로 생성안함.
                
            .and()    
                .authorizeRequests()
                .antMatchers("/users/**","/posts","/posts/detail/**","/login",
                             "/oauth2/**","o").permitAll()
                .antMatchers("/posts/**","/").hasRole("MEMBER") //자동으로 앞에 "ROLE_"이 삽입 
                .anyRequest().authenticated()  //  로그인된 사용자가 요청을 수행할 떄 필요하다  만약 사용자가 인증되지 않았다면, 스프링 시큐리티 필터는 요청을 잡아내고 사용자를 로그인 페이지로 리다이렉션 해준다
            .and()
                .logout()
                .logoutSuccessUrl("/")
            .and()
                .oauth2Login()

                //.userInfoEndpoint()
                //.userService(oAuth2UserService)
            .and()                                              
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
}


    /*.authenticationEntryPoint(authenticationEntryPoint)
    .accessDeniedHandler(accessDeniedHandler) */   
/*.and()    
    .formLogin()
    .usernameParameter("accountId")//loadUserByUsername 접근함
    .successHandler(successHandler) //토큰 생성하게함*/