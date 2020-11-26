package com.innilabs.restboard.dto.req;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PostReq {
    private String title;
    private String contents;
    
    @JsonIgnore
    private int postNo;
    
    @JsonIgnore
    private String username;
}