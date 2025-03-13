package com.mycompany.teamproject9.controller;

import com.mycompany.teamproject9.service.RecaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recaptcha")
public class RecaptchaController {

    private final RecaptchaService recaptchaService;
    @Autowired
    public RecaptchaController(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    //    private final RecaptchaConfig recaptchaConfig;
//    @Autowired
//    public RecaptchaController(RecaptchaConfig recaptchaConfig) {
//        this.recaptchaConfig = recaptchaConfig;
//    }

    @PostMapping("/verify")
    public boolean verifyRecaptcha(@RequestParam("g-recaptcha-response") String recaptchaResponse) {
        return recaptchaService.verifyRecaptcha(recaptchaResponse);
    }
}