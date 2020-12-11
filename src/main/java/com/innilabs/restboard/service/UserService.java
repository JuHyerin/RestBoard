package com.innilabs.restboard.service;

import java.util.ArrayList;
import java.util.List;

import com.innilabs.restboard.auth.JwtProvider;
import com.innilabs.restboard.dto.req.AccountReq;
import com.innilabs.restboard.entity.Account;
import com.innilabs.restboard.mapper.AccountMapper;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder; //spring security에서 제공해주는 암호화 인터페이스
    private final JwtProvider tokenProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountMapper.readAccount(username);
        if( account == null ) {
			log.debug("## 계정정보가 존재하지 않습니다. ##");
			throw new UsernameNotFoundException(username);
        }        
        account.setAuthorities(getAuthorities(username));
        return account;
    }

    public List<GrantedAuthority> getAuthorities(String username){
        List<String> stringAuthority = accountMapper.readAuthority(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        stringAuthority.forEach(s->{authorities.add(new SimpleGrantedAuthority(s));});

        return authorities;
    } 

    public int save(AccountReq accountReq) throws Exception, DuplicateKeyException {
        accountReq.setPassword(passwordEncoder.encode(accountReq.getPassword()));                               
        int isSaved = accountMapper.insertAccount(accountReq);
        if(isSaved<1){
            throw new Exception("회원가입 실패");
        }
        accountMapper.insertAuthority(accountReq.getEmail(), accountReq.getRole());
        
        return isSaved;
    }

	public String signIn(AccountReq accountReq) {
        String username = accountReq.getEmail();
        Account account = accountMapper.readAccount(accountReq.getEmail());
        if( account == null ) {
			log.debug("## 계정정보가 존재하지 않습니다. ##");
            //throw new UsernameNotFoundException(accountReq.getAccountId());
            return null;
        }
        
        if(account.getPassword()!=null && passwordEncoder.matches(accountReq.getPassword(), account.getPassword() ) ){
            List<String> stringAuthority = accountMapper.readAuthority(username); //jwt
            account.setRoles(stringAuthority);
            List<GrantedAuthority> authorities = new ArrayList<>(); //userdetails
            for(String authority : stringAuthority){
                authorities.add(new SimpleGrantedAuthority(authority));
            }
            account.setAuthorities(authorities);
             
            String token = tokenProvider.createToken(account);
            return token;
        }
        else if(account.getPassword() == null){
            List<String> stringAuthority = accountMapper.readAuthority(username); //jwt
            account.setRoles(stringAuthority);
            List<GrantedAuthority> authorities = new ArrayList<>(); //userdetails
            for(String authority : stringAuthority){
                authorities.add(new SimpleGrantedAuthority(authority));
            }
            account.setAuthorities(authorities);
             
            String token = tokenProvider.createToken(account);
            return token;
        }
    
		return null;
	}
}