package com.innilabs.restboard.handler;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = DuplicateKeyException.class)
    public String handlerBoardException(DuplicateKeyException e){
        /* if(e.getType()==1){
            //예외 종류에 따라 메세지 변경
        } 
        log.error("일치하는 게시물 없음",e);
        return "<h1>"+e.getMessage()+"</h1>";
        */
        log.error(e.getMessage(), e);
        return "<h1>키값중복</h1>";
    }

    
}