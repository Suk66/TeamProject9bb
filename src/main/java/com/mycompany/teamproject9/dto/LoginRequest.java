package com.mycompany.teamproject9.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String pwd;
    private String recaptchaResponse; // reCAPTCHA 응답을 받을 필드 추가
}