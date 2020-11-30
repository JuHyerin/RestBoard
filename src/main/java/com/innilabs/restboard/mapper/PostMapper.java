package com.innilabs.restboard.mapper;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.innilabs.restboard.dto.req.ListReq;
import com.innilabs.restboard.dto.req.PostReq;
import com.innilabs.restboard.entity.Account;
import com.innilabs.restboard.entity.Post;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

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
			"VALUES (#{post.title}, #{post.contents}, #{post.username}, now())")
	@Options(useGeneratedKeys=true, keyProperty="postId")
	int insertPost(@Param("post") PostReq post);


	/* @Select("SELECT writer FROM POST WHERE post_id = #{postId}")
	String selectWriterByPostId(String username, int postId); */
	@Select("SELECT COUNT(*) FROM POST "+
			"WHERE is_deleted = 0 "+
			"AND post_id = #{postId} "+
			"AND writer = #{username")
	int countPostWithUsername(@Param("username") String username, @Param("postId") int postId);


	@Update("UPDATE POST SET contents = #{post.contents}, updated_at = now() "+
			"WHERE is_deleted = 0 AND post_id = #{post.postNo}")/*  AND writer = #{post.username}") */
	int updatePostByPostId(@Param("post") PostReq postReq);
	


}