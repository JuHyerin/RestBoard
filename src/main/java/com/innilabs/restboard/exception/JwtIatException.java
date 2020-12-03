package com.innilabs.restboard.exception;

import org.springframework.security.oauth2.jwt.JwtException;

public class JwtIatException extends JwtException {
    
    public JwtIatException(String message) {
        super(message);
    }

    public JwtIatException(String message, Throwable cause) {
        super(message, cause);
    }
}