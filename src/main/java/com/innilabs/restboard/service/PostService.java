package com.innilabs.restboard.service;

import java.util.List;

import com.innilabs.restboard.auth.JwtDto;
import com.innilabs.restboard.dto.req.ListReq;
import com.innilabs.restboard.dto.req.PostReq;
import com.innilabs.restboard.dto.res.ErrorCode;
import com.innilabs.restboard.dto.res.ResObj;
import com.innilabs.restboard.entity.Post;
import com.innilabs.restboard.mapper.PostMapper;
import com.innilabs.restboard.util.PagingUtil;
import com.innilabs.restboard.util.StringUtil;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostMapper postMapper;

	final static int POST_PAGE_SIZE = 3;
	final static int POST_PAGE_BLOCK_SIZE = 2;

	final static int COMMENT_PAGE_SIZE = 3;
	final static int COMMENT_PAGE_BLOCK_SIZE = 2;
	
	public ResObj getAllPosts(Integer page, String option, String word) {
		ListReq listReq = new ListReq();
		listReq.setOption(option);
		listReq.setWord('%'+word+'%');
		PagingUtil paging = new PagingUtil(page, POST_PAGE_SIZE, POST_PAGE_BLOCK_SIZE);
		paging.setTotalData(postMapper.countPost(listReq));

		listReq.setPageSize(POST_PAGE_SIZE);
		listReq.setStartIndex(paging.getFirstData());
		List<Post> posts = postMapper.selectAllPosts(listReq);
		if(posts.size()==0){
			return new ResObj(ErrorCode.NO_DATA);
		}

		return new ResObj(ErrorCode.SUCCESS, posts);
	}

	
	public ResObj getPostDetail(int postId, Integer commentPage) {
		PagingUtil paging = new PagingUtil(commentPage, COMMENT_PAGE_SIZE, COMMENT_PAGE_BLOCK_SIZE);
		int countComment = postMapper.countComment(postId);
		if (countComment == 0){
			commentPage = 0; //댓글 없으면 페이징 x
		}
		paging.setTotalData(countComment);
		if(commentPage>paging.getTotalPages()){
			return new ResObj(ErrorCode.INVALID_PARAMETER);
		}
		//Post post = postMapper.selectPostWithComments(postId);
		Post post = postMapper.selectPostByPostId(postId);
		if(post==null){
			return new ResObj(ErrorCode.NO_DATA);
		}
		post.setComments(postMapper.getComments(postId, paging));

		return new ResObj(ErrorCode.SUCCESS, post);
	}


	public ResObj createPost(PostReq postReq) {
		JwtDto user = (JwtDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		if(username==null){
			return new ResObj(ErrorCode.NOT_MEMBER);
		}

		postReq.setUsername(username);
		int isSaved = postMapper.insertPost(postReq);
		if (isSaved < 1) {
			return new ResObj(ErrorCode.FAILED_TO_CREATE);
		} 
		
		return new ResObj(ErrorCode.SUCCESS , postReq.getPostId());
	}
	

	public ResObj updatePost(PostReq postReq) {
		if(!checkWriter(postReq.getPostId())){
			return new ResObj(ErrorCode.MEMBER_NOT_PRINCIPAL);
		}

		int isUpdated = postMapper.updatePostByPostId(postReq);
		if(isUpdated<1){
			return new ResObj(ErrorCode.FAILED_TO_UPDATE);
		}

		return new ResObj(ErrorCode.SUCCESS);
	}


	public ResObj deletePost(int postId) {
		if(!checkWriter(postId)){
			return new ResObj(ErrorCode.MEMBER_NOT_PRINCIPAL);
		}

		int isUpdated = postMapper.deletePostByPostId(postId);
		if(isUpdated<1){
			return new ResObj(ErrorCode.FAILED_TO_UPDATE);
		}

		return new ResObj(ErrorCode.SUCCESS);
	}


	public boolean checkWriter(int postId){
		JwtDto principal = (JwtDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = principal.getUsername();

		String writer = postMapper.selectWriter(postId, username);
		if(!StringUtil.isEmpty(writer)){
			return true;
		}
		return false;
	}

} 