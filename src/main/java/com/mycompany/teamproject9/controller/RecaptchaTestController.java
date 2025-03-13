package com.mycompany.teamproject9.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")  // 개발 환경에서만 활성화됨
@RestController
public class RecaptchaTestController {

    @Value("${recaptcha.site-key}")
    private String siteKey;

    @GetMapping("/recaptcha-test")
    public String testRecaptchaKeys() {
        return "Site Key: " + siteKey;
    }
}