package com.srecon.authenticator.oauth.controller;

import com.srecon.authenticator.oauth.cache.AzureAPIStore;
import com.srecon.authenticator.oauth.model.AzureAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AzureOauthController {

    @Autowired
    AzureAPIStore azureAPIStore;

    @GetMapping(value = "oauth/azure/authenticate")
    public ResponseEntity<AzureAuthResponse> authenticate() {
        AzureAuthResponse response = azureAPIStore.refreshAzureAuthIfNecessary();
        return ResponseEntity.of(Optional.of(response));
    }
}
