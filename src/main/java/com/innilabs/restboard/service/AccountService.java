package com.innilabs.restboard.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.innilabs.restboard.dto.req.AccountReq;
import com.innilabs.restboard.dto.res.SavedAccount;
import com.innilabs.restboard.dto.res.SavedAccount.SavedAccountBuilder;
import com.innilabs.restboard.entity.Account;
import com.innilabs.restboard.mapper.AccountMapper;
import com.innilabs.restboard.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
public class AccountService implements UserDetailsService {
    
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder; //spring security에서 제공해주는 암호화 인터페이스

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountMapper.readAccount(username);
        if( account == null ) {
			log.debug("## 계정정보가 존재하지 않습니다. ##");
			throw new UsernameNotFoundException(username);
		}
    
        return new User(account.getAccountId(), account.getPassword(), getAuthorities(username));
    }

    public Collection<GrantedAuthority> getAuthorities(String username){
        List<String> stringAuthority = accountMapper.readAuthority(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String authority : stringAuthority){
            authorities.add(new SimpleGrantedAuthority(authority));
        }

        return authorities;
    }

    public Account save(AccountReq accountReq) throws Exception {
        
        accountReq.setPassword(passwordEncoder.encode(accountReq.getPassword()));                               
        int isSaved = accountMapper.insertAuthority(accountReq.getAccountId(), accountReq.getRole());
        
        if(isSaved<1){
            throw new Exception("회원가입 실패");
        }
        return new Account(0,accountReq.getAccountId(), accountReq.getPassword());
    }
}