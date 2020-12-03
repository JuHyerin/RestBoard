package com.innilabs.restboard.controller;

import java.util.List;

import com.innilabs.restboard.dto.req.PostReq;
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

    static final String REDIRECT_LIST_PAGE = "redirect:/posts/list"; 
   
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
        if(StringUtil.isEmpty(word)){
            word="";
        }
        List<Post> posts = postService.getAllPosts(page, option, word);
        ResObj resObj = posts.size()<1
                        ? ResObj.builder()
                                .code("400")
                                .msg("모든 게시물 조회 실패")
                                .contents(posts)
                                .build()
                        : ResObj.builder()
                                .code("200")
                                .msg("모든 게시물 조회 성공")
                                .contents(posts)
                                .build();
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }
    
    @GetMapping("/posts/detail/{postId}")
    public ResponseEntity<?> detailPost(@PathVariable("postId") int postId, int commentPage){
        Post post = postService.getPostDetail(postId, commentPage);
        ResObj resObj = ResObj.builder()
                                .contents(post)
                                .build();
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody PostReq postReq) {
        int isCreated = postService.createPost(postReq);
        ResObj resObj = isCreated<1
                        ? ResObj.builder()
                                .code("400")
                                .msg("게시물 생성 실패")
                                .build()
                        : ResObj.builder()
                                .code("200")
                                .msg("게시물 생성 성공")
                                .build();
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable("postId") int postId, @RequestBody PostReq postReq) throws BoardException {
        int isUpdated = postService.updatePost(postId, postReq);
        ResObj resObj = isUpdated<1
                        ? ResObj.builder()
                                .code("400")
                                .msg("게시물 수정 실패")
                                .build()
                        : ResObj.builder()
                                .code("200")
                                .msg("게시물 수정 성공")
                                .build();
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    } 

    @GetMapping("/posts/delete/{postId}") // 삭제는 민감 -> PostMapping으로 구현
    public ResponseEntity<?> deletePost(@PathVariable("postId") int postId) throws BoardException {
        int isDeleted = postService.deletePost(postId);
        ResObj resObj = isDeleted<1
                        ? ResObj.builder()
                                .code("400")
                                .msg("게시물 삭제 실패")
                                .build()
                        : ResObj.builder()
                                .code("200")
                                .msg("게시물 삭제 성공")
                                .build();
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }


    /* @PostAuthorize("isAuthenticated() and returnObject.writer==principal.username")
    @PutMapping("/posts/{id}")
    public ResponseEntity<Integer> updatePost(@PathVariable int postId,
                                            @RequestBody PostReq postReq) {
        //int isUpdated = postService.updatePost(postId, postReq);
        return new ResponseEntity<Integer>(isUpdated, HttpStatus.OK);
    } */
}