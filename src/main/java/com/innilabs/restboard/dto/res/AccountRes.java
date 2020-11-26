package com.innilabs.restboard.dto.res;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccountRes {
    private String accountId;
    private String password;
    private String role;
}