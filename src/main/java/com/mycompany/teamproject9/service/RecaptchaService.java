package com.mycompany.teamproject9.service;

import com.mycompany.teamproject9.config.RecaptchaConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@AllArgsConstructor
@Service
public class RecaptchaService {
    private final RecaptchaConfig recaptchaConfig;
    private final RestTemplate restTemplate;

    public boolean verifyRecaptcha(String recaptchaResponse) {
        String url = "https://www.google.com/recaptcha/api/siteverify";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", recaptchaConfig.getSecretKey());
        params.add("response", recaptchaResponse);

        // Google reCAPTCHA 검증 API에 요청
        ResponseEntity<Map> response = restTemplate.postForEntity(url, params, Map.class);
        Map<String, Object> body = response.getBody();

        if (body != null && (boolean) body.get("success")) {
            return true; // ✅ reCAPTCHA 인증 성공
        }
        return false;
    }
}