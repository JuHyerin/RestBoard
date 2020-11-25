package com.innilabs.restboard.mapper;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.innilabs.restboard.dto.req.ListReq;
import com.innilabs.restboard.dto.req.PostReq;
import com.innilabs.restboard.entity.Post;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
public interface PostMapper {
	
	/* @Select("SELECT post_id, title, A.account_id as writer, contents, created_at "+
			"FROM POST "+
			"LEFT JOIN ACCOUNT A "+
			"ON writer = A.seq "+
			"WHERE is_deleted = 0") */
	@Select("SELECT post_id, title, writer, contents, created_at "+
			"FROM POST "+
			"WHERE is_deleted = 0")
	List<Post> selectAllPosts();
	

	@Insert("INSERT INTO POST (title, contents, writer, created_at) "+
			"VALUES (#{post.title}, #{post.contents}, #{writer}, #{createdAt}) ")
	@SelectKey(statement="SELECT LAST_INSERT_ID()",
				 keyProperty="post_id", resultType=Integer.class, before = false) 
	int insertPost(@Param("post") PostReq post, 
					@Param("writer") String username, 
					@Param("createdAt") LocalDateTime createdAt);


}