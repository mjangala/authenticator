package com.srecon.authenticator.oauth.service;

import com.srecon.authenticator.oauth.model.AzureAuthResponse;
import com.srecon.authenticator.oauth.model.OktaTokenDefinition;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;
@Component
public class AzureOAuthConnector {

    private static final String DOMAIN = "login.microsoftonline.com";
    private static final String TENANT_ID = "3a83fefa-3473-4318-ac59-e6ffd43440b8";
    private static final String CLIENT_ID = "beb80f88-cb17-4969-b55d-fbcf1ac3b0f7";
    private static final String CLIENT_SECRET = "54h8Q~CG-tlRruP5vr~DgSG2UU8DvUQfuG33ndkB";
    private static final String GRANT_TYPE = "client_credentials";
    private static final String SCOPE = "api://0eba20e4-7a65-4cfc-90f6-0ee08417f7c6/.default";

    public AzureAuthResponse authenticate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", GRANT_TYPE);
        map.add("client_id", CLIENT_ID);
        map.add("client_secret", CLIENT_SECRET);
        map.add("scope", SCOPE);
        HttpEntity<?> entity = new HttpEntity<>(map, headers);
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(String.format("https://%s/%s/oauth2/v2.0/token", DOMAIN, TENANT_ID))
                .build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<AzureAuthResponse> response = null;
        try {
            response = template.exchange(uriComponents.toUriString(), HttpMethod.POST, entity, AzureAuthResponse.class);
            Objects.requireNonNull(response.getBody()).populateExpiresAt();
        } catch (RestClientException ex) {
            System.out.println("Unable to fetch access token: " + ex.getMessage());
        }
        return response == null ? null : response.getBody();
    }

    public OktaTokenDefinition tokenDefinition(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(String.format("https://%s/oauth2/default/v1/introspect", DOMAIN))
                .queryParam("client_id", CLIENT_ID)
                .queryParam("client_secret", CLIENT_SECRET)
                .queryParam("scope", SCOPE)
                .queryParam("token", token)
                .queryParam("token_type_hint", "access_token")
                .build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<OktaTokenDefinition> response = null;
        try {
            response = template.exchange(uriComponents.toUriString(), HttpMethod.POST, entity, OktaTokenDefinition.class);
        } catch (RestClientException ex) {
            System.out.println("Unable to fetch access token: " + ex.getMessage());
        }
        return response == null ? null : response.getBody();
    }
}
