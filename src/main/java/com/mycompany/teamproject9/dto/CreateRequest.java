package com.mycompany.teamproject9.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRequest {
    private String userType;    // admin 또는 customer
    private String name;
    private String email;
    private String pwd;
    private String phone;
    private String addr;        // customer만 입력
}