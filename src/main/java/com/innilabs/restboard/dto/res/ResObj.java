package com.innilabs.restboard.dto.res;

import lombok.Builder;
import lombok.Data;

@Builder
@Data //JSON으로 변환할때 Getter 꼭 필요함
public class ResObj {
    String msg;
    String code;
    Object contents; 
}