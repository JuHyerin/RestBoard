package com.innilabs.restboard.exception;

import org.springframework.security.oauth2.jwt.JwtException;

public class JwtExpException extends JwtException{

    private static final long serialVersionUID = 1L;

    public JwtExpException(String message) {
        super(message);
    }

    public JwtExpException(String message, Throwable cause) {
        super(message, cause);
    }
    
}