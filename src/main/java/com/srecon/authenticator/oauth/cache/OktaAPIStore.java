package com.srecon.authenticator.oauth.cache;

import com.srecon.authenticator.oauth.model.OktaAuthResponse;
import com.srecon.authenticator.oauth.model.OktaTokenDefinition;
import com.srecon.authenticator.oauth.service.OktaOAuthConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OktaAPIStore {

    private OktaAuthResponse oktaAuthResponse;

    @Autowired
    private OktaOAuthConnector oktaOAuthConnector;

    public OktaAuthResponse refreshOktaAuthIfNecessary() {
        if (oktaAuthResponse == null) {
            oktaAuthResponse = oktaOAuthConnector.authenticate();
        } else {
            if (LocalDateTime.now().isAfter(oktaAuthResponse.getExpiresAt()))
                oktaAuthResponse = oktaOAuthConnector.authenticate();
        }
        System.out.println(oktaAuthResponse.toString());
        return oktaAuthResponse;
    }

    public OktaTokenDefinition fetchOktaDefinition() {
        OktaAuthResponse response = refreshOktaAuthIfNecessary();
        return oktaOAuthConnector.tokenDefinition(response.getAccessToken());
    }
}
