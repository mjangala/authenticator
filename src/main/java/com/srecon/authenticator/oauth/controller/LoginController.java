package com.srecon.authenticator.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("okta/success")
    public String oktaWelcome() {
        return "Oauth Authentication successful from Okta. Welcome to Smart Recon!!";
    }

    @GetMapping("azure/success")
    public String azureWelcome() {
        return "Oauth Authentication successful from Azure. Welcome to Smart Recon!!";
    }

    @GetMapping("okta/saml/success")
    public String oktaSamlWelcome() {
        return "Saml Authentication successful from Okta. Welcome to Smart Recon!!";
    }
}
