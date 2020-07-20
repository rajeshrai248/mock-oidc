package com.realdolmen.oidc.provider.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;

import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;
    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;
    @Autowired
    private ServerProperties serverProperties;


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        String baseUrl = serverProperties.getAddress() + ":" + serverProperties.getPort();
        clients
                .inMemory()
                .withClient(clientId)
                .secret(passwordEncoder().encode(clientSecret))
                .scopes("read write openid oidc:bric")
                .authorizedGrantTypes("authorization_code")
                .redirectUris("http://" + baseUrl + "/oauth/login/client-app");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return createDelegatingPasswordEncoder();
    }
}
