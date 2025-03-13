package com.mycompany.teamproject9.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "recaptcha")
public class RecaptchaConfig {
    @Value("${recaptcha.site-key}")
    private String siteKey;
    @Value("${recaptcha.secret-key}")
    private String secretKey;
}