package com.innilabs.restboard.service;

import java.util.List;

import com.innilabs.restboard.auth.JwtDto;
import com.innilabs.restboard.dto.req.ListReq;
import com.innilabs.restboard.dto.req.PostReq;
import com.innilabs.restboard.entity.Post;
import com.innilabs.restboard.exception.BoardException;
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


	public List<Post> getAllPosts(int page, String option, String word) {
		ListReq listReq = new ListReq();
		listReq.setOption(option);
		listReq.setWord('%'+word+'%');

		PagingUtil paging = new PagingUtil(page, POST_PAGE_SIZE, POST_PAGE_BLOCK_SIZE);
		paging.setTotalData(postMapper.countPost(listReq));

		listReq.setPageSize(POST_PAGE_SIZE);
		listReq.setStartIndex(paging.getFirstData());

		List<Post> posts = postMapper.selectAllPosts(listReq);
		if(posts.size()==0){
			posts.add(new Post());
		}
		return posts;
	}

	public int createPost(PostReq postReq) {
		JwtDto user = (JwtDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		if(username==null){
			//throw new BoardException("인증되지 않은 사용자");
			return 0;
		}
		postReq.setUsername(username);
		int isSaved = postMapper.insertPost(postReq);
		/* if (isSaved < 1) {
			throw new BoardException("게시물 생성 실패.");	
		} */
		return isSaved;
	}

	public int updatePost(int postId, PostReq postReq) throws BoardException {
		JwtDto principal = (JwtDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = principal.getUsername();

		if(StringUtil.isEmpty(postReq.getContents()) ){
			//변경할 내용 없음
			return 0;
		}
		postReq.setUsername(username);
		postReq.setPostId(postId);
		int isUpdated = postMapper.updatePostByPostId(postReq);

		if(isUpdated<1){
			throw new BoardException("수정 실패.");
		}
		return isUpdated;
	}

	public int deletePost(int postId) throws BoardException {
		JwtDto principal = (JwtDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = principal.getUsername();

		int isdeleted = postMapper.deletePostByPostId(postId, username);

		if(isdeleted<1){
			throw new BoardException("삭제 실패.");
		}
		return isdeleted;
	}

	public Post getPostDetail(int postId, int commentPage) {
		PagingUtil paging = new PagingUtil(commentPage, COMMENT_PAGE_SIZE, COMMENT_PAGE_BLOCK_SIZE);
		paging.setTotalData(postMapper.countComment(postId));

		Post post = postMapper.selectPostWithComments(postId, paging);

		return post;
	}
	
    
} 