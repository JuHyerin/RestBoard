package com.innilabs.restboard.controller;

import java.util.List;

import com.innilabs.restboard.dto.req.PostReq;
import com.innilabs.restboard.dto.res.ErrorCode;
import com.innilabs.restboard.dto.res.ResObj;
import com.innilabs.restboard.entity.Post;
import com.innilabs.restboard.exception.BoardException;
import com.innilabs.restboard.service.PostService;
import com.innilabs.restboard.util.StringUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
   
    @GetMapping("/")
     public String hi(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "<h1>hi "+username+"</h1>";
    } 

    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts(Integer page, String option, String word){
        if(page==null || page==0){
            page = 1;
        }
        if(StringUtil.isEmpty(option)){
            option = "title";
        }
        if(StringUtil.isEmpty(word)){
            word = "";
        }
        ResObj resObj = postService.getAllPosts(page, option, word);
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }


    /* @GetMapping("/posts/detail/{postId}")
    public ResponseEntity<?> detailPost(@PathVariable("postId") int postId, int commentPage){
        Post post = postService.getPostDetail(postId, commentPage);
        ResObj resObj = ResObj.builder()
                                .contents(post)
                                .build();
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    } */
    @GetMapping("/posts/detail/{postId}")
    public ResponseEntity<?> detailPost(@PathVariable("postId") int postId, Integer commentPage){
        if(commentPage==null || commentPage==0){
            commentPage = 1;
        }
        ResObj resObj = postService.getPostDetail(postId, commentPage);
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }


    @PostMapping("/posts/create")
    public ResponseEntity<?> createPost(@RequestBody PostReq postReq) {
        ResObj resObj= postService.createPost(postReq);
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }


    @PostMapping("/posts/update")
    public ResponseEntity<?> updatePost(@RequestBody PostReq postReq){
        ResObj resObj= postService.updatePost(postReq);
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    } 

  
    @PostMapping("/posts/delete")
    public ResponseEntity<?> deletePost(@RequestBody int postId){
        ResObj resObj = postService.deletePost(postId);
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }
}