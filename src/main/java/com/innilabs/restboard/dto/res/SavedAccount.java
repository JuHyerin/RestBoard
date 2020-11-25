package com.innilabs.restboard.dto.res;

import org.springframework.security.core.GrantedAuthority;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SavedAccount {
    private String accountId;
    private String password;
    private GrantedAuthority authorities;

}