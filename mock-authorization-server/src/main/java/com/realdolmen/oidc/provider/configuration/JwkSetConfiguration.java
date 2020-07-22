package com.realdolmen.oidc.provider.configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PublicKey;
import org.apache.tomcat.util.net.jsse.PEMFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@Configuration
public class JwkSetConfiguration extends AuthorizationServerConfigurerAdapter {

    private AuthenticationManager authenticationManager;

    @Value("${security.oauth2.provider.keystore-location}")
    private String keyStoreLocation;

    @Value("${security.oauth2.provider.keystore-pwd}")
    private String keyStorePwd;

    @Autowired
    public JwkSetConfiguration(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // @formatter:off
        endpoints
                .authenticationManager(this.authenticationManager)
                .accessTokenConverter(accessTokenConverter())
                .tokenStore(tokenStore());
        // @formatter:on
    }

    @Bean
    public TokenStore tokenStore() throws Exception {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws GeneralSecurityException, IOException {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(getSigningKey());
        return converter;
    }

    @Bean
    public KeyPair getSigningKey() throws IOException, GeneralSecurityException {
        if (isEmpty(keyStoreLocation)) {
            throw new IllegalArgumentException("key store location cant be null");
        }
        PEMFile pemFile = new PEMFile(keyStoreLocation, keyStorePwd);
        PublicKey publicKey = pemFile.getCertificates().get(0).getPublicKey();
        return new KeyPair(publicKey, pemFile.getPrivateKey());
    }

}
