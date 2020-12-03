package com.innilabs.restboard.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class Comment {
    private int commentId;

    private String writer;

    private String contents;

    private LocalDateTime createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updatedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime deletedAt;

    @JsonIgnore
    private boolean deleted;

    /* @JsonInclude(JsonInclude.Include.NON_NULL)
    private Post post; */
}