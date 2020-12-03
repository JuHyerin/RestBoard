package com.innilabs.restboard.dto.req;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class PostReq {
    
    private String contents;
    
    private String title;
    
    private int postId;
    
    @JsonIgnore
    private String username;
}