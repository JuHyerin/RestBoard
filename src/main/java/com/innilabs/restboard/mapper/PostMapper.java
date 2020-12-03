package com.innilabs.restboard.mapper;

import java.util.List;

import com.innilabs.restboard.dto.req.ListReq;
import com.innilabs.restboard.dto.req.PostReq;
import com.innilabs.restboard.entity.Comment;
import com.innilabs.restboard.entity.Post;
import com.innilabs.restboard.util.PagingUtil;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PostMapper {
	
	@Select({"<script> ",
			"SELECT post_id, title, writer, created_at, updated_at ",
			"FROM POST ",
			"WHERE is_deleted=0 ",
			"<if test='\"title\".equals(option)'>AND title LIKE #{word} </if> ",
			"<if test='\"writer\".equals(option)'>AND writer LIKE #{word} </if> ",
			"ORDER BY created_at DESC ",
	  		"LIMIT #{startIndex}, #{pageSize} ",
			"</script>"})
	List<Post> selectAllPosts(ListReq listReq);
	

	@Insert({"INSERT INTO POST (title, contents, writer, created_at) ",
			"VALUES (#{post.title}, #{post.contents}, #{post.username}, now())"})
	@Options(useGeneratedKeys=true, keyProperty="postId")
	int insertPost(@Param("post") PostReq post);


	@Update({"<script> ",
			"UPDATE POST SET ",
			"<if test='title != null'> title = #{title}, </if> ",
			"<if test='contents != null'> contents = #{contents}, </if> ",
			"updated_at = now() ",
			"WHERE post_id = #{postId} AND writer = #{username}",
			"</script>"})	
	int updatePostByPostId(PostReq postReq);

	@Update({"UPDATE POST SET ",
			"deleted_at = now(), ",
			"is_deleted = 1 ",
			"WHERE post_id = #{postId} AND writer = #{username}"})	
	int deletePostByPostId(@Param("postId") int postId, @Param("username") String username);

	@Select({"<script> ",
			"SELECT COUNT(*) ",
			"FROM POST ",
			"WHERE is_deleted=0 ",
			"<if test='\"title\".equals(option)'>AND title LIKE #{word}</if> ",
			"<if test='\"writer\".equals(option)'>AND writer LIKE #{word}</if> ",
			"</script>"})
	int countPost(ListReq listReq);

	@Select("{SELECT COUNT(*) ",
			"FROM COMMENT ",
			"WHERE is_deleted = 0 AND post_id = #{postid} "})
	int countComment(int postId);

	@Results({
		@Result(property="comments", javaType=List.class, column="post_id", 
				many=@Many(select = "getComments"))
	})
	@Select({"SELECT post_id, title, created_at, updated_at, writer, contents ",
			"FROM POST ",
			"WHERE post_id = #{postId} ",
			"AND is_deleted = 0 "})
	Post selectPostWithComments(@Param("postId") int postId, PagingUtil paging);

	@Select({"SELECT * ",
			"FROM COMMENT ",
			"WHERE is_deleted = 0 AND post_id = #{postId} ",
			"ORDER BY created_at DESC "})
	List<Comment> getComments(@Param("postId") int postId);
}