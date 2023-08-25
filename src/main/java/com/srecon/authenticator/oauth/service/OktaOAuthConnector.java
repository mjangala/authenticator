package com.srecon.authenticator.oauth.service;

import com.srecon.authenticator.oauth.model.OktaAuthResponse;
import com.srecon.authenticator.oauth.model.OktaTokenDefinition;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
public class OktaOAuthConnector {

    private static final String DOMAIN = "dev-45335417.okta.com";
    private static final String CLIENT_ID = "0oaaqv2wt3sPw1udy5d7";
    private static final String CLIENT_SECRET = "pUElPM_6dF2cCEpwFDXTYseo0w04zILJy9fUvNnvoO5kfTSEQY-VX_teNhxGUWDT";
    private static final String GRANT_TYPE = "client_credentials";
    private static final String SCOPE = "smartrecon";

    public OktaAuthResponse authenticate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(String.format("https://%s/oauth2/default/v1/token", DOMAIN))
                .queryParam("client_id", CLIENT_ID)
                .queryParam("client_secret", CLIENT_SECRET)
                .queryParam("grant_type", GRANT_TYPE)
                .queryParam("scope", SCOPE)
                .build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<OktaAuthResponse> response = null;
        try {
            response = template.exchange(uriComponents.toUriString(), HttpMethod.POST, entity, OktaAuthResponse.class);
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
