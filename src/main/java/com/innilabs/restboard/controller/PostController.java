package com.innilabs.restboard.controller;

import java.util.List;

import com.innilabs.restboard.dto.req.PostReq;
import com.innilabs.restboard.entity.Post;
import com.innilabs.restboard.service.PostService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    

    static final String REDIRECT_LIST_PAGE = "redirect:/posts/list"; 

    @GetMapping("/")
     public String hi(){
        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
        String username = principal.getUsername(); 
        return "<h1>hi "+username+"</h1>";
    } 

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
    }
    
    @PostMapping("/posts")
    public ResponseEntity<Integer> createPost(@RequestBody PostReq postReq){
        int postId = postService.createPost(postReq);
        return new ResponseEntity<>(postId, HttpStatus.OK);
    }
}