package com.innilabs.restboard.auth;

import java.util.List;
import java.util.Map;

import com.innilabs.restboard.dto.req.AccountReq;
import com.innilabs.restboard.entity.Account;
import com.innilabs.restboard.mapper.AccountMapper;
import com.innilabs.restboard.util.AuthUtil;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyOAuth2UserService extends DefaultOAuth2UserService {
    private final AccountMapper accountMapper;
    
    @Override  //OAuth2Provider로 부터 AccessToken 얻은 후 호출됨 -> 사용자 정보 저장 또는 업데이트
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
      
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                                    .getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes oAuthattributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes); 

        Account account = saveOrUpate(oAuthattributes);
        Authentication authentication = new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        return account;
    }

    private Account saveOrUpate(OAuthAttributes oAuthattributes){
        String email = oAuthattributes.getEmail();
        Account account = accountMapper.readAccount(email);
        
        if(account == null){
            AccountReq accountReq = oAuthattributes.toDto();
            accountMapper.insertAccount(accountReq);
            accountMapper.insertAuthority(email, accountReq.getRole());
            account = oAuthattributes.toEntity();
            account.setAttributes(oAuthattributes.getAttributes());
            return account;
        }
        
        List<String> roles = accountMapper.readAuthority(email);
        account.setRoles(roles);
        account.setAuthorities(AuthUtil.rolesToAuthorities(roles));
        account.update(oAuthattributes.getName(), oAuthattributes.getPicture());
        accountMapper.updateAccount(account);
        account.setAttributes(oAuthattributes.getAttributes());
        return account;
    }
    
}