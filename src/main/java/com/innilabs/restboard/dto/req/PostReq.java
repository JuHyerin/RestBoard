package com.innilabs.restboard.dto.req;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PostReq {
    private String title;
    private String contents;
}