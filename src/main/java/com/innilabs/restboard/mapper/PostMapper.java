package com.innilabs.restboard.mapper;

import java.util.List;

import com.innilabs.restboard.dto.req.ListReq;
import com.innilabs.restboard.dto.req.PostReq;
import com.innilabs.restboard.entity.Comment;
import com.innilabs.restboard.entity.Post;
import com.innilabs.restboard.util.PagingUtil;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PostMapper {
	
	@Select({"<script> ",
			"SELECT post_id, title, writer, created_at, updated_at ",
			"FROM post ",
			"WHERE is_deleted = 0 ",
			"<if test='\"title\".equals(option)'>AND title LIKE #{word} </if> ",
			"<if test='\"writer\".equals(option)'>AND writer LIKE #{word} </if> ",
			"ORDER BY created_at DESC ",
	  		"LIMIT #{startIndex}, #{pageSize} ",
			"</script>"})
	List<Post> selectAllPosts(ListReq listReq);
	

	@Insert({"INSERT INTO post (title, contents, writer, created_at) ",
			"VALUES (#{title}, #{contents}, #{username}, now())"})
	@Options(useGeneratedKeys=true, keyProperty="postId")
	int insertPost(PostReq post);


	@Update({"<script> ",
			"UPDATE post SET ",
			"<if test='title != null'> title = #{title}, </if> ",
			"<if test='contents != null'> contents = #{contents}, </if> ",
			"updated_at = now() ",
			"WHERE post_id = #{postId} ",
			"AND is_deleted = 0 ",
			"</script>"})	
	int updatePostByPostId(PostReq postReq);

	
	@Update({"UPDATE post SET ",
			"deleted_at = now(), ",
			"is_deleted = 1 ",
			"WHERE post_id = #{postId}"})	
	int deletePostByPostId(int postId);


	@Select({"<script> ",
			"SELECT COUNT(*) ",
			"FROM post ",
			"WHERE is_deleted = 0 ",
			"<if test='\"title\".equals(option)'>AND title LIKE #{word}</if> ",
			"<if test='\"writer\".equals(option)'>AND writer LIKE #{word}</if> ",
			"</script>"})
	int countPost(ListReq listReq);


	@Select({"SELECT COUNT(*) ",
			"FROM COMMENT ",
			"WHERE is_deleted = 0 AND post_id = #{postid} "})
	int countComment(int postId);
	/* @Results({
		@Result(property="comments", javaType=List.class, column="post_id", 
				many=@Many(select = "getComments"))
	})
	@Select({"SELECT post_id, title, created_at, updated_at, writer, contents ",
			"FROM post ",
			"WHERE post_id = #{postId} AND is_deleted = 0 "})
	Post selectPostWithComments(@Param("postId") int postId);
	@Select({"SELECT * ",
			"FROM COMMENT ",
			"WHERE is_deleted = 0 AND post_id = #{postId} ",
			"ORDER BY created_at DESC "})
	List<Comment> getComments(@Param("postId") int postId); */
	@Select({"SELECT post_id, title, created_at, updated_at, writer, contents ",
			"FROM post ",
			"WHERE post_id = #{postId} AND is_deleted = 0 "})
	Post selectPostByPostId(int postId);
	@Select({"SELECT * ",
			"FROM COMMENT ",
			"WHERE is_deleted = 0 AND post_id = #{postId} ",
			"ORDER BY created_at DESC ",
			"LIMIT #{paging.firstData}, #{paging.pageSize}"})
	List<Comment> getComments(@Param("postId") int postId, @Param("paging") PagingUtil paging);


	@Select({"SELECT writer ",
			"FROM post ",
			"WHERE is_deleted = 0 AND writer = #{username} AND post_id = #{postId}"})
	String selectWriter(@Param("postId") int postId, @Param("username") String username);
}