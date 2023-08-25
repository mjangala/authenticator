package com.srecon.authenticator.oauth.cache;

import com.srecon.authenticator.oauth.model.AzureAuthResponse;
import com.srecon.authenticator.oauth.service.AzureOAuthConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class AzureAPIStore {

    private AzureAuthResponse azureAuthResponse;

    @Autowired
    private AzureOAuthConnector azureOAuthConnector;

    public AzureAuthResponse refreshAzureAuthIfNecessary() {
        if (azureAuthResponse == null) {
            azureAuthResponse = azureOAuthConnector.authenticate();
        } else {
            if (LocalDateTime.now().isAfter(azureAuthResponse.getExpiresAt()))
                azureAuthResponse = azureOAuthConnector.authenticate();
        }
        System.out.println(azureAuthResponse.toString());
        return azureAuthResponse;
    }

//    public AzureAuthResponse fetchOktaDefinition() {
//        AzureAuthResponse response = refreshAzureAuthIfNecessary();
//        return azureOAuthConnector.tokenDefinition(response.getAccessToken());
//    }
}
