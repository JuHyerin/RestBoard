package com.innilabs.restboard.controller;

import com.innilabs.restboard.dto.req.AccountReq;
import com.innilabs.restboard.dto.res.AccountRes;
import com.innilabs.restboard.dto.res.ResObj;
import com.innilabs.restboard.entity.Account;
import com.innilabs.restboard.service.AccountService;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;

    
    @PostMapping("/users")
    public ResponseEntity<Integer> createdUser(@RequestBody AccountReq account) throws Exception, DuplicateKeyException {
    
        int isSaved = accountService.save(account);
        return new ResponseEntity<Integer>(isSaved, HttpStatus.OK);

    }
    
    @GetMapping("users/denied")
    public ResponseEntity<ResObj> deniedUser(){
        //return "<h1>권한 없는 사용자</h1>";
        ResObj resObj = ResObj.builder()
                                .msg("권한 없는 사용자")
                                .code("403")
                                .build();
        return new ResponseEntity<ResObj>(resObj, HttpStatus.OK);
    }
}