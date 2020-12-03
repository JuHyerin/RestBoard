package com.innilabs.restboard.controller;


import com.innilabs.restboard.dto.req.AccountReq;
import com.innilabs.restboard.dto.res.ResObj;
import com.innilabs.restboard.service.UserService;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;

    //토큰 내려주는 역할
    @PostMapping("/users/signin")
    public ResponseEntity<?> userSignIn(@RequestBody AccountReq accountReq){
        String token = userService.signIn(accountReq);
        ResObj resObj;
        if(token==null) {
            resObj = ResObj.builder()
                            .msg("로그인 실패")
                            .contents(accountReq.getAccountId())
                            .code("401")
                            .build();
            
        } else {
            resObj = ResObj.builder()
                            .contents(token)
                            .msg("로그인 성공")
                            .code("200")
                            .build();
        }
        return new ResponseEntity<>(resObj, HttpStatus.OK);
}

    @PostMapping("/users/signout")
    public ResponseEntity<?> userSignOut(){
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<Integer> createdUser(@RequestBody AccountReq account) throws Exception, DuplicateKeyException {
        int isSaved = userService.save(account);
        return new ResponseEntity<Integer>(isSaved, HttpStatus.OK);
    }
    
/*     @GetMapping("users/denied")
    public ResponseEntity<ResObj> deniedUser(){
        //return "<h1>권한 없는 사용자</h1>";
        ResObj resObj = ResObj.builder()
                                .msg("권한 없는 사용자")
                                .code("403")
                                .build();
        return new ResponseEntity<ResObj>(resObj, HttpStatus.OK);
    } */
}