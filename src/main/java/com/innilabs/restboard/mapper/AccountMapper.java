package com.innilabs.restboard.mapper;

import java.util.List;

import com.innilabs.restboard.dto.req.AccountReq;
import com.innilabs.restboard.entity.Account;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountMapper {

    @Select("SELECT account_id as username, password, email, name FROM ACCOUNT WHERE account_id=#{id}")
    public Account readAccount(String accountId);
    
    @Select("SELECT post_id FROM POST WHERE writer=#{id}")
    public List<String> readPosts(String accountId);

    @Select("SELECT authority_name FROM AUTHORITY WHERE username=#{id}")
    public List<String> readAuthority(String accountId);
    
    @Insert("INSERT INTO ACCOUNT (account_id, password) VALUES (#{account.accountId}, #{account.password})")
    public int insertAccount(@Param("account") AccountReq account);
    
    @Insert("INSERT INTO AUTHORITY (username, authority_name) VALUES (#{username}, #{role})")
    public int insertAuthority(@Param("username") String accountId, @Param("role") String role); 
    
}