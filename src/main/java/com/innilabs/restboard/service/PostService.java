package com.innilabs.restboard.service;

import java.time.LocalDateTime;
import java.util.List;

import com.innilabs.restboard.dto.req.PostReq;
import com.innilabs.restboard.entity.Account;
import com.innilabs.restboard.entity.Post;
import com.innilabs.restboard.exception.BoardException;
import com.innilabs.restboard.mapper.PostMapper;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

	private final PostMapper postMapper;

	public List<Post> getAllPosts() {
		List<Post> posts = postMapper.selectAllPosts();
		return posts;
	}

	public int createPost(PostReq postReq) throws BoardException {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(username==null){
			throw new BoardException("인증되지 않은 사용자");
		}
		postReq.setUsername(username);
		int isSaved = postMapper.insertPost(postReq);
		if (isSaved < 1) {
			throw new BoardException("게시물 생성 실패.");	
		}
		return postReq.getPostId();
	}

	public int updatePost(int postId, PostReq postReq) throws BoardException {
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = principal.getUsername();
		//postId의 작성자인지 확인
		if(postMapper.countPostWithUsername(username, postId) < 1){
			throw new BoardException("작성자만 수정 가능.");
		}
		//postId인 게시물 수정
		//postReq.setUsername(username);
		postReq.setPostId(postId);
		int isUpdated = postMapper.updatePostByPostId(postReq);
		if(isUpdated<1){
			throw new BoardException("수정 실패.");
		}
		return isUpdated;
	}
	
    
} 