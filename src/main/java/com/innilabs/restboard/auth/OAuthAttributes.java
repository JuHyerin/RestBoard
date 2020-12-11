package com.innilabs.restboard.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.innilabs.restboard.dto.req.AccountReq;
import com.innilabs.restboard.entity.Account;
import com.innilabs.restboard.util.AuthUtil;
import com.innilabs.restboard.util.StringUtil;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Builder;
import lombok.Getter;

//Creation Pattern 참고
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String,Object> attributes){
        //소셜에 따라 Map의 key가 다름 -> registrationId로 구분해서 따로 OAuthAttributes 객체 만들어줘야함
        switch(registrationId){
            case "google": return ofGoogle(userNameAttributeName, attributes);
            case "naver": return ofNaver(userNameAttributeName, attributes);
        }
        return null;
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        return OAuthAttributes.builder()
                                .name((String)attributes.get("name"))
                                .email((String)attributes.get("email"))
                                .picture((String)attributes.get("picture"))
                                .attributes(attributes)
                                .nameAttributeKey(userNameAttributeName)
                                .build();
    }

    //Naver는 resource가 attributes(Map) 밑의 "resource"의 value로 있음(Map)
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> naverAttributes = (Map<String, Object>) attributes.get("response"); 
        return OAuthAttributes.builder()
                                .name((String)naverAttributes.get("name"))
                                .email((String)naverAttributes.get("email"))
                                .picture((String)naverAttributes.get("profile_image"))
                                .attributes(naverAttributes)
                                .nameAttributeKey(userNameAttributeName)
                                .build();
    }

    public Account toEntity(){ //save
        Account account = new Account();
        account.setName(name);
        account.setUsername(email);
        account.setPicture(picture);
        ArrayList<String> roles = new ArrayList<>();
        roles.add("ROLE_MEMBER");
        account.setRoles(roles);
        account.setAuthorities(AuthUtil.rolesToAuthorities(roles));
        return account;
    }


    public AccountReq toDto(){ //save
        AccountReq accountReq = new AccountReq();
        accountReq.setEmail(email);
        accountReq.setName(name);
        accountReq.setPicture(picture);
        accountReq.setRole("ROLE_MEMBER");

        return accountReq;
    }
}