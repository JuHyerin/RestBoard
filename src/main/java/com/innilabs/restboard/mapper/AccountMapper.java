package com.innilabs.restboard.mapper;

import java.util.List;

import com.innilabs.restboard.dto.req.AccountReq;
import com.innilabs.restboard.entity.Account;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AccountMapper {

    @Select("SELECT email as username, password, name, picture FROM account WHERE email=#{email}")
    public Account readAccount(String email);
    
    @Select("SELECT post_id FROM post WHERE writer=#{email}")
    public List<String> readPosts(String email);

    @Select("SELECT authority_name FROM authority WHERE username=#{email}")
    public List<String> readAuthority(String email);
    
    @Insert("INSERT INTO account (email, password,name,picture) VALUES (#{email}, #{password},#{name},#{picture})")
    public int insertAccount(AccountReq account);
    
    @Insert("INSERT INTO authority (username, authority_name) VALUES (#{username}, #{role})")
    public int insertAuthority(@Param("username") String email, @Param("role") String role); 

    @Update("UPDATE account SET name = #{name}, picture = #{picture} WHERE email = #{username}")
    public int updateAccount(Account account);
    
}