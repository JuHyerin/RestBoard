package com.innilabs.restboard.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

//@Builder
@Data //JSON으로 변환할때 Getter 꼭 필요함
public class ResObj {
    String msg;
    int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object contents; 

    public ResObj(ErrorCode condition){
        this.code = condition.getCode();
        this.msg = condition.getMsg();
    }

    public ResObj(ErrorCode condition, Object contents){
        this.code = condition.getCode();
        this.msg = condition.getMsg();
        this.contents = contents;
    }
}