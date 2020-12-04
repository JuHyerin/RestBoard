package com.innilabs.restboard.auth;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class JwtDto {
    private String username;//email
    private String name;
    //private String email;
    private List<String> roles = new ArrayList<>();
}