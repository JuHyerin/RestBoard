package com.innilabs.restboard.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Post implements Serializable{

	private int postId;

	private String title;
	
	private String writer;
	
	private String contents;
	
	private LocalDateTime createdAt;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime updatedAt;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime deletedAt;
	
	private boolean isDeleted;
}