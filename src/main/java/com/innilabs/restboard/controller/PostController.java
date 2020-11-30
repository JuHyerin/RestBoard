package com.innilabs.restboard.controller;

import java.util.List;

import com.innilabs.restboard.dto.req.PostReq;
import com.innilabs.restboard.entity.Post;
import com.innilabs.restboard.exception.BoardException;
import com.innilabs.restboard.service.PostService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "<h1>hi "+username+"</h1>";
    } 

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
    }
    
    @PostMapping("/posts/create")
    public ResponseEntity<Integer> createPost(@RequestBody PostReq postReq) throws BoardException {
        int createdPostId = postService.createPost(postReq);
        return new ResponseEntity<>(createdPostId, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<Integer> updatePost(@PathVariable("postId") int postId, @RequestBody PostReq postReq) throws BoardException {
        int isUpdated = postService.updatePost(postId, postReq);
        return new ResponseEntity<Integer>(isUpdated, HttpStatus.OK);
    } 
    /* @PostAuthorize("isAuthenticated() and returnObject.writer==principal.username")
    @PutMapping("/posts/{id}")
    public ResponseEntity<Integer> updatePost(@PathVariable int postId,
                                            @RequestBody PostReq postReq) {
        //int isUpdated = postService.updatePost(postId, postReq);
        return new ResponseEntity<Integer>(isUpdated, HttpStatus.OK);
    } */
}