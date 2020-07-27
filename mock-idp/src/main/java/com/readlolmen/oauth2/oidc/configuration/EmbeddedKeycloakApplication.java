package com.readlolmen.oauth2.oidc.configuration;

import com.readlolmen.oauth2.oidc.properties.KeycloakServerProperties;
import org.keycloak.Config;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.services.managers.ApplianceBootstrap;
import org.keycloak.services.managers.RealmManager;
import org.keycloak.services.resources.KeycloakApplication;
import org.keycloak.services.util.JsonConfigProviderFactory;
import org.keycloak.util.JsonSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class EmbeddedKeycloakApplication extends KeycloakApplication {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedKeycloakApplication.class);
    public static final String BRICIAM = "briciam";
    static KeycloakServerProperties keycloakServerProperties;

    protected void loadConfig() {
        JsonConfigProviderFactory factory = new RegularJsonConfigProviderFactory();
        Config.init(factory.create().orElseThrow(() -> new NoSuchElementException("No value present")));
    }

    public EmbeddedKeycloakApplication() {
        createMasterRealmAdminUser();
        createBricIamRealm();
    }

    private void createMasterRealmAdminUser() {
        KeycloakSession session = getSessionFactory().create();
        ApplianceBootstrap applianceBootstrap = new ApplianceBootstrap(session);
        KeycloakServerProperties.AdminUser admin = keycloakServerProperties.getAdminUser();
        try {
            session.getTransactionManager().begin();
            applianceBootstrap.createMasterRealmUser(admin.getUsername(), admin.getPassword());
            session.getTransactionManager().commit();
        } catch (Exception ex) {
            LOG.warn("Couldn't create keycloak master admin user: {}", ex.getMessage());
            session.getTransactionManager().rollback();
        }
        session.close();
    }

    private void createBricIamRealm() {
        KeycloakSession session = getSessionFactory().create();
        try {
            session.getTransactionManager().begin();
            RealmManager manager = new RealmManager(session);
            Resource lessonRealmImportFile = new ClassPathResource(keycloakServerProperties.getRealmImportFile());
            manager.importRealm(
                    JsonSerialization.readValue(lessonRealmImportFile.getInputStream(), RealmRepresentation.class));
            setClientDetails(manager, keycloakServerProperties.getClients());

            session.getTransactionManager().commit();
        } catch (Exception ex) {
            LOG.warn("Failed to import Realm json file: {}", ex.getMessage());
            session.getTransactionManager().rollback();
        }
        session.close();
    }

    private void setClientDetails(RealmManager manager, Map<String, KeycloakServerProperties.Client> clients) {
        RealmModel bricIamModel = manager.getRealmByName(BRICIAM);
        clients.forEach((clientId, clientDetails) -> {
            ClientModel clientModel = bricIamModel.addClient(UUID.randomUUID().toString(), clientId);
            clientModel.setName(clientDetails.getClientName());
            clientModel.setRedirectUris(clientDetails.getClientRedirectUris());
            clientModel.setEnabled(true);
            clientModel.setClientAuthenticatorType(clientDetails.getClientAuthenticationType());
            clientModel.setSecret(clientDetails.getClientSecret());
            clientModel.setProtocol(clientDetails.getClientProtocol());
            clientModel.setWebOrigins(Collections.singleton("+"));
            clientModel.setRootUrl(clientDetails.getBaseUrl());
        });

    }
}
