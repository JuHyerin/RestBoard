package com.innilabs.restboard.service;

import java.time.LocalDateTime;
import java.util.List;

import com.innilabs.restboard.dto.req.PostReq;
import com.innilabs.restboard.entity.Post;
import com.innilabs.restboard.mapper.PostMapper;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    
	private final PostMapper postMapper;
	
	public List<Post> getAllPosts() {
		List<Post> posts = postMapper.selectAllPosts();
		return posts;
	}

	public int createPost(PostReq postReq) {
		User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		String username = principal.getUsername(); 
		LocalDateTime createdAt = LocalDateTime.now();
		int postId = postMapper.insertPost(postReq, username, createdAt);
		return postId;
	}
    

	/* public int createPost(Post post) {
        int createPost = postMapper.insertPost(post);
		return createPost;
	}
 */
	
    
}