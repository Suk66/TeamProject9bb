package com.mycompany.teamproject9.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String name;
    private String email;
    private String phone;
    private String addr;     // 관리자는 null 가능
    private String userType; // "customer" 또는 "admin"

    private String password; // 추가된 비밀번호 필드
}