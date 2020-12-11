package com.innilabs.restboard.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.innilabs.restboard.util.StringUtil;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.filter.OncePerRequestFilter;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtProvider tokenProvider;
    public JwtAuthenticationFilter(JwtProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

    // Header에서 Token 추출하여 유효성 검사 -> Authentication을 SecurityContextHolder에 등록
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        
        String token = tokenProvider.resolveJwtToken(request, response);
        if (!StringUtil.isEmpty(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            // accountService.loadUserByUsername(user.getUsername());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}