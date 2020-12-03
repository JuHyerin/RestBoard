package com.innilabs.restboard.handler;

import com.innilabs.restboard.exception.BoardException;

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
    public String handleDuplicateKeyException(DuplicateKeyException e){
    
        log.error("handler:" + e.getMessage(), e);
        return "<h1>키값중복</h1>";
        
    }

    @ExceptionHandler(value = Exception.class)
    public String handlerException(Exception e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler(value = BoardException.class)
    public String handlerBoardException(BoardException e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }
    /* @ExceptionHandler(value = DuplicateKeyException.class)
    public ResponseEntity<ResObj> handleDuplicateKeyException(DuplicateKeyException e){
    
        log.error("handler:" + e.getMessage(), e);
        //return "<h1>키값중복</h1>";
        ResObj body = ResObj.builder()
                .msg("아이디 중복되어 생성 실패")
                .code("000")
                .build();
        return new ResponseEntity<ResObj>(body, HttpStatus.OK);
    } */

    // @ExceptionHandler(value = Exception.class)
    // public ResponseEntity<ResObj> handlerBoardException(Exception e){
    //     /* if(e.getType()==1){
    //         //예외 종류에 따라 메세지 변경
    //     } 
    //     log.error("일치하는 게시물 없음",e);
    //     return "<h1>"+e.getMessage()+"</h1>";
    //     */
    //     log.error(e.getMessage(), e);
    //     ResObj body = ResObj.builder()
    //             .msg(e.getMessage())
    //             .build();
    //     return new ResponseEntity<ResObj>(body, HttpStatus.OK);
    // }
    
}