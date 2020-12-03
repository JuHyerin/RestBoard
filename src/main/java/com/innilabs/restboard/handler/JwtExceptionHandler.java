package com.innilabs.restboard.handler;

import com.innilabs.restboard.dto.res.ErrorCode;
import com.innilabs.restboard.dto.res.ResObj;
import com.innilabs.restboard.exception.JwtExpException;
import com.innilabs.restboard.exception.JwtIatException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RestController
@Slf4j
public class JwtExceptionHandler {
    @ExceptionHandler(value = JwtExpException.class)
    public ResponseEntity<?> handleJwtExpException(JwtExpException e){
        log.error("handler:" + e.getMessage(), e);
        ResObj resObj = new ResObj(ErrorCode.EXPIRED_TOKEN);
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }

    @ExceptionHandler(value = JwtIatException.class)
    public ResponseEntity<?> handleJwtIatException(JwtIatException e){
        log.error("handler:" + e.getMessage(), e);
        ResObj resObj = new ResObj(ErrorCode.INVALID_TOKEN);
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }
}