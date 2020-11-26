package com.innilabs.restboard.dto.res;

import lombok.Builder;

@Builder
public class ResObj {
    String msg;
    String code;
    Object contents; 
}