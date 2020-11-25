package com.innilabs.restboard.dto.res;

import com.innilabs.restboard.entity.Post;
import com.innilabs.restboard.util.PagingUtil;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailRes {
    private Post post;
    private PagingUtil paging;
}