package com.springbootmicroservices.userservice.config;

import com.springbootmicroservices.userservice.utils.SSLUtil;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

@Configuration
public class KeycloakConfig {

    public final static String serverUrl = "https://keycloak-http.default.svc.cluster.local:8443";
    public final static String realm = "master";
    public final static String clientId = "spring-boot-microservice-keycloak";
    public final static String clientSecret = "snW4v6floZNsQ410FqPm2jxApITNmTe2";
    final static String userName = "admin";
    final static String password = "admin";

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    public Keycloak keycloak() {
        SSLContext sslContext = SSLUtil.getInsecureSSLContext();

        ResteasyClient client = new ResteasyClientBuilder()
                .sslContext(sslContext)
                .hostnameVerifier((hostname, session) -> true)
                .build();

        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(userName)
                .password(password)
                .resteasyClient(client)
                .build();
    }
}
