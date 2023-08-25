package com.srecon.authenticator.oauth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OktaTokenDefinition {

    private boolean active;
    private String scope;
    private long exp;
    private long iat;
    private String sub;
    private String aud;
    private String iss;
    private String jti;
    @JsonProperty(value = "token_type")
    private String tokenType;
    @JsonProperty(value = "client_id")
    private String clientId;
}
