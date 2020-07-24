package com.realdolmen.oidc.client.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

//@Configuration
public class ClientConfiguration {
    @Value("${spring.security.oauth2.client.provider.mock.token-uri}")
    private String tokenUri;

    @Bean("oauth2RestTemplate")
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri(tokenUri)
                .build();
    }
}