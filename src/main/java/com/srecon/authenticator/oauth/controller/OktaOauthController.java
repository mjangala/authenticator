package com.srecon.authenticator.oauth.controller;


import com.srecon.authenticator.oauth.cache.OktaAPIStore;
import com.srecon.authenticator.oauth.model.OktaAuthResponse;
import com.srecon.authenticator.oauth.model.OktaTokenDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class OktaOauthController {

    @Autowired
    OktaAPIStore oktaAPIStore;

    @GetMapping(value = "oauth/okta/authenticate")
    public ResponseEntity<OktaAuthResponse> authenticate() {
        OktaAuthResponse response = oktaAPIStore.refreshOktaAuthIfNecessary();
        return ResponseEntity.of(Optional.of(response));
    }

    @GetMapping(value = "oauth/okta/token/info")
    public ResponseEntity<OktaTokenDefinition> tokenInfo() {
        OktaTokenDefinition response = oktaAPIStore.fetchOktaDefinition();
        return ResponseEntity.of(Optional.of(response));
    }
}
