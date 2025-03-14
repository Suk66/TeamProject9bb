package com.mycompany.teamproject9.controller;

import com.mycompany.teamproject9.config.RecaptchaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recaptcha")
public class RecaptchaController {

    private final RecaptchaConfig recaptchaConfig;

    @Autowired
    public RecaptchaController(RecaptchaConfig recaptchaConfig) {
        this.recaptchaConfig = recaptchaConfig;
    }

    @GetMapping("/site-key")
    public String getRecaptchaSiteKey() {
        return recaptchaConfig.getSiteKey();
    }
}