package com.innilabs.restboard.dto.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountReq {
    private String accountId; //loadUserByUsername의 파라미터랑 필드이름 맞춰줘야함(username || security config에서 설정도 가능)
    private String password;
    private String role;
}