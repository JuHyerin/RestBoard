package com.innilabs.restboard.dto.res;

import java.util.List;

import com.innilabs.restboard.entity.Post;
import com.innilabs.restboard.util.PagingUtil;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ListRes {
    private List<Post> posts;
    private PagingUtil paging;
    //검색 view에서 default로 출력하는 용도
    private String option;
    private String word;

}