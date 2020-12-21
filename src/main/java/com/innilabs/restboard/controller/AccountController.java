package com.innilabs.restboard.controller;


import com.innilabs.restboard.dto.req.AccountReq;
import com.innilabs.restboard.dto.res.ErrorCode;
import com.innilabs.restboard.dto.res.ResObj;
import com.innilabs.restboard.service.UserService;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/hi")
    public ResponseEntity<?> hi(){
        ResObj resObj = userService.greet();
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }


    //토큰 내려주는 역할
    @PostMapping("/users/signin")
    public ResponseEntity<?> userSignIn(@RequestBody AccountReq accountReq){
        ResObj resObj = userService.signIn(accountReq);
        if(resObj==null) {
            resObj = new ResObj(ErrorCode.NOT_FOUND_USER, accountReq.getEmail()); 
        } 
        return new ResponseEntity<>(resObj, HttpStatus.OK);
}

    @PostMapping("/users/signout")
    public ResponseEntity<?> userSignOut(){
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/users/create")
    public ResponseEntity<Integer> createdUser(@RequestBody AccountReq account) throws Exception, DuplicateKeyException {
        int isSaved = userService.save(account);
        return new ResponseEntity<Integer>(isSaved, HttpStatus.OK);
    }
    
}