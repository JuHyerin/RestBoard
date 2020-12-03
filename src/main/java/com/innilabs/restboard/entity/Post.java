package com.innilabs.restboard.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Post implements Serializable{

	private int postId;

	private String title;
	
	private String writer;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String contents;
	
	private LocalDateTime createdAt;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime updatedAt;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime deletedAt;
	
	@JsonIgnore
	private boolean deleted;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Comment> comments;
}