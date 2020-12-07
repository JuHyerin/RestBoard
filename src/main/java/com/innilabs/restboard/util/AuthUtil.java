package com.innilabs.restboard.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthUtil {
    public static List<GrantedAuthority> rolesToAuthorities(List<String> roles){
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(e->{ authorities.add(new SimpleGrantedAuthority(e)); });

        return authorities;
    }
}