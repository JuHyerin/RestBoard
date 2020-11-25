package com.innilabs.restboard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
public class Account {
    private int seq;
    private String accountId;
    private String password;
}