package com.innilabs.restboard.dto.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountReq {
    private String accountId;
    private String password;
    private String role;
}