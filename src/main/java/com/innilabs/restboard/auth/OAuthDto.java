package com.innilabs.restboard.auth;

import com.innilabs.restboard.entity.Account;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthDto {
    private String email;
    private String name;
    private String picture;

    public static OAuthDto from(Account account){
        return OAuthDto.builder()
                        .email(account.getUsername())
                        .name(account.getName())
                        .picture(account.getPicture())
                        .build();
    }
}