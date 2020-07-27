package com.readlolmen.oauth2.oidc.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "keycloak.server")
@Getter
@Setter
public class KeycloakServerProperties {

    String contextPath;
    String realmImportFile;
    AdminUser adminUser = new AdminUser();
    Map<String, Client> clients = new HashMap<>();

    @Getter
    @Setter
    public static class AdminUser {

        String username;
        String password;
    }

    @Getter
    @Setter
    public static class Client {

        private String clientName;
        private String clientAuthenticationType;
        private String clientSecret;
        private String clientProtocol;
        private String baseUrl;
        private Set<String> clientRedirectUris;
        private Boolean directAccessGrantsEnabled = true;
        private Boolean implicitFlowControlEnabled = false;
    }

}
