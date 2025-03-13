package com.mycompany.teamproject9.service;

import com.mycompany.teamproject9.config.RecaptchaConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
@AllArgsConstructor
@Service
public class RecaptchaService {
    private final RecaptchaConfig recaptchaConfig;
    private final RestTemplate restTemplate;

//    public RecaptchaService(RecaptchaConfig recaptchaConfig, RestTemplate restTemplate, RestTemplate restTemplate1) {
//        this.recaptchaConfig = recaptchaConfig;
//        this.restTemplate = restTemplate1;
//    }
//    @Value("${recaptcha.site-key}")
//    private String siteKey;
//
//    @Value("${recaptcha.secret-key}")
//    private String secretKey;


    public boolean verifyRecaptcha(String recaptchaResponse) {
        String url = "https://www.google.com/recaptcha/api/siteverify";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", recaptchaConfig.getSecretKey());
        params.add("response", recaptchaResponse);

        // Google reCAPTCHA 검증 API에 요청
        ResponseEntity<String> response = restTemplate.postForEntity(url, params, String.class);

        // 응답에서 success 값을 확인
        return response.getBody().contains("\"success\": true");
    }
}