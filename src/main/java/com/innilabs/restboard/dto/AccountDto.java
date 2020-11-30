package com.innilabs.restboard.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AccountDto {
    String username;
    List<String> roles=new ArrayList<>();
}