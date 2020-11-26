package com.innilabs.restboard.repository;

import java.util.Collection;
import java.util.List;

import com.innilabs.restboard.dto.req.AccountReq;
import com.innilabs.restboard.entity.Account;
import com.innilabs.restboard.entity.Authority;
import com.innilabs.restboard.mapper.AccountMapper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AccountRepository {

    private final AccountMapper accountMapper;

	public Account findById(String accountId) {
		return accountMapper.readAccount(accountId);
	}

    public List<String> findAuthorityById(String accountId){
        return accountMapper.readAuthority(accountId);
	}
/* 	
	public int save(CreatedAccount account) throws Exception {
		System.out.println("repo" + account);
//        int isAuthSaved = accountMapper.insertAuthority(account);
int isAuthSaved = accountMapper.insertAuthority(account.getAccountId(), account.getRole());

		int isAccountSaved = accountMapper.insertAccount(account);
System.out.println("권한 등록 여부" + isAuthSaved);
System.out.println("사용자 등록 여부" + isAccountSaved);

		if(isAuthSaved<1 || isAccountSaved<1){
			log.error("error{}: Reop.save");
			throw new Exception("회원가입 실패");
		}
		return isAccountSaved;
	} */

	public int save(AccountReq savedAccount) {
		int isAuthSaved = accountMapper.insertAuthority(savedAccount.getAccountId(),savedAccount.getRole());
		int isAccountSaved = accountMapper.insertAccount(savedAccount);


		return isAccountSaved;
	} 
}