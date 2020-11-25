package com.innilabs.restboard.controller;

import com.innilabs.restboard.dto.req.AccountReq;
import com.innilabs.restboard.dto.res.SavedAccount;
import com.innilabs.restboard.entity.Account;
import com.innilabs.restboard.service.AccountService;

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
    public Account createdUser(@RequestBody AccountReq account) throws Exception {
        try {
            return accountService.save(account);
        } catch (Exception e) {
            log.error("Conroller: 회원가입 실패", e);
            throw e;
        }
    }
    
    @GetMapping("users/denied")
    public String deniedUser(){
        return "<h1>권한 없는 사용자</h1>";
    }
}