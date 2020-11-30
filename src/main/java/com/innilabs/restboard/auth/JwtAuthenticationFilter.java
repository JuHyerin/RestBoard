package com.innilabs.restboard.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.innilabs.restboard.util.StringUtil;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

//import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtProvider tokenProvider;

    /* @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
            String token = tokenProvider.resolveJwtToken((HttpServletRequest)request);
            if (!StringUtil.isEmpty(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                // accountService.loadUserByUsername(user.getUsername());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
    
            chain.doFilter(request, response);
    } */
    public JwtAuthenticationFilter(JwtProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String token = tokenProvider.resolveJwtToken((HttpServletRequest)request);
                if (!StringUtil.isEmpty(token)) {
                    Authentication authentication = tokenProvider.getAuthentication(token);
                    // accountService.loadUserByUsername(user.getUsername());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

                filterChain.doFilter(request, response);

    }







    // Header에서 Token 추출하여 유효성 검사 -> Authentication을 SecurityContextHolder에 등록
   /* 
   @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = tokenProvider.resolveJwtToken(request);
        if (!StringUtil.isEmpty(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            // accountService.loadUserByUsername(user.getUsername());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    } */

    
}