package com.readlolmen.oauth2.oidc.properties;

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

    @Getter
    @Setter
    public static class AdminUser {
        String username;
        String password;
    }
}
