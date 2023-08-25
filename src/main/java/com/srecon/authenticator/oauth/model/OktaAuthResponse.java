package com.srecon.authenticator.oauth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OktaAuthResponse {

    @JsonProperty(value = "token_type")
    private String tokenType;
    @JsonProperty(value = "expires_in")
    private long expiresInSeconds;
    @JsonProperty(value = "access_token")
    private String accessToken;
    @JsonProperty(value = "scope")
    private String scope;

    @JsonIgnore
    private LocalDateTime expiresAt;

    public void populateExpiresAt(){
       expiresAt = LocalDateTime.now().plusSeconds(expiresInSeconds);
    }
}
