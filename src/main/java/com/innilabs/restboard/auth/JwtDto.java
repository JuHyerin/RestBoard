package com.innilabs.restboard.auth;

import java.util.ArrayList;
import java.util.List;

import com.innilabs.restboard.entity.Account;

import lombok.Data;

@Data
public class JwtDto {
    private String username;//email
    private String name;
    private String picture;
    private List<String> roles = new ArrayList<>();

    private String token; //Vuex

    public static JwtDto of (Account account){ 
        JwtDto dto = new JwtDto();
        dto.setUsername(account.getUsername());
        dto.setName( account.getName());
        dto.setPicture(account.getPicture());
        dto.setRoles(account.getRoles());

        return dto;
    }
}