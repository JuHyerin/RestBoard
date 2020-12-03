package com.innilabs.restboard.entity;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class Account implements UserDetails{
	@JsonIgnore
	private int seq;
	
	private String username;
	
	private String password;

	private String email;

	private String name;

	//DB에서 authorities 가져올 때 각각 타입(GrantedAuhtority, String)에 맞게 넣어줌
	@JsonIgnore
    Collection<? extends GrantedAuthority> authorities; // json으로 변환이 안됨
	private List<String> roles; // jwt 변환용
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { //맞춰주는게 좋음 누가 부를지 몰라
		return this.authorities;
	}
	@Override
	public String getUsername() {
		return this.username;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	@Override
	public String getPassword() {
		return this.password;
	}
    
}