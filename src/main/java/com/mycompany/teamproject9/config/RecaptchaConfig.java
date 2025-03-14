package com.mycompany.teamproject9.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "recaptcha")
public class RecaptchaConfig {

    private String siteKey;
    private String secretKey;

    public RecaptchaConfig() {
        this.siteKey = System.getenv("recaptcha_site_key");
        this.secretKey = System.getenv("recaptcha_secret_key");
    }
}