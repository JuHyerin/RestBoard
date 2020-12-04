package com.innilabs.restboard.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.json.DupDetector;
import com.innilabs.restboard.dto.req.AccountReq;
import com.innilabs.restboard.entity.Account;
import com.innilabs.restboard.mapper.AccountMapper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyOAuth2UserService implements OAuth2UserService {
    private final AccountMapper accountMapper;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                                    .getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes oAuthattributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes); 

        Account account = saveOrUpate(oAuthattributes);
        Authentication authentication = new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new DefaultOAuth2User(
                    account.getAuthorities(), 
                    oAuthattributes.getAttributes(), 
                    oAuthattributes.getNameAttributeKey());
    }

    private Account saveOrUpate(OAuthAttributes oAuthattributes){
        Account account = accountMapper.readAccount(oAuthattributes.getEmail());
        
        if(account == null){
            accountMapper.insertAccount(oAuthattributes.toDto());
            return oAuthattributes.toEntity();
        }
        
        account.setAuthorities(getAuthorities(oAuthattributes.getEmail()));
        account.update(oAuthattributes.getName(), oAuthattributes.getPicture());
        accountMapper.updateAccount(account);
        return account;
    }
    
    public List<GrantedAuthority> getAuthorities(String username){
        List<String> stringAuthority = accountMapper.readAuthority(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        stringAuthority.forEach(s->{authorities.add(new SimpleGrantedAuthority(s));});

        return authorities;
    } 
}