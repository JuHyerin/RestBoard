package com.innilabs.restboard.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum ErrorCode {
    SUCCESS (1000, "Success"),

    // GENERAL 2000
    INVALID_PARAMETER (2000, "Invalid parameter"),
    // INVALID_HEADER (2002, "Invalid header"),
    NOT_SUPPORTED(2001, "Not Supported"),
    NO_DATA(2002, "No data"),
    NOT_FOUND_USER(2003, "Not found user"),
    NOT_LINKED_LINE_ACCOUNT(2004, "Not linked line account"),
    NOT_MEMBER(2005, "Not a member"),
    ALREADY_EXISTS(2006, "Already Exists"),
    
    // AUTH 3000
    INVALID_TOKEN (3000, "Invalid auth token"),  //  jwt token이 유효하지 않음
    EXPIRED_TOKEN (3001, "This token is expired"),  // jwt token의 유효기간 경과
    INVALID_DEVICEID(3002, "Expired deviceID access"),  // Device ID 중복사용으로 무효화
    UNAUTHORIZED(3003, "Access is not allowed"), //인증 필요 경로에 token없이 접근
    MEMBER_ALLOWED(3004, "Member only allowed"),  // Permitted only to customer with valid account
    MEMBER_NOT_ALLOWED(3005, "Member not allowed"), //security에서 필터링됨
    MEMBER_NOT_PRINCIPAL(3006, "Member not principal"), //작성자가 아님

    //POST FAILURE 4000
    FAILED_TO_CREATE(4001, "Failed to create post"),
    FAILED_TO_UPDATE(4002, "Failed to update post"),
    FAILED_TO_DELETE(4003, "Failed to delete post");

    private final int code;
    private final String msg;
}