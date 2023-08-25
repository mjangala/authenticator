package com.srecon.authenticator.oauth.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        OpenSaml4AuthenticationProvider authenticationProvider = new OpenSaml4AuthenticationProvider();
//        authenticationProvider.setResponseAuthenticationConverter(groupConverter());
        http.authorizeRequests().antMatchers("/oauth/**").permitAll().and()
                .authorizeRequests().antMatchers("/login").authenticated()
                //.and().oauth2Login(t -> t.clientRegistrationRepository(oktaClientRegistrationRepository()));
                .and().oauth2Login(t -> t.clientRegistrationRepository(azureClientRegistrationRepository()));
                //.authorizeRequests().antMatchers("/saml/login").authenticated();
//                .and().saml2Login(t -> {
//                    try {
//                        t.relyingPartyRegistrationRepository(new InMemoryRelyingPartyRegistrationRepository(createRelyingPartyRegistration()));
//                    } catch (Exception e) {
//                        System.out.println(e);
//                    }
//                });
                        //authenticationManager(new ProviderManager(authenticationProvider)));
    }

    private RelyingPartyRegistration createRelyingPartyRegistration() throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException, UnrecoverableEntryException {
        final InputStream is = new ByteArrayInputStream(("-----BEGIN CERTIFICATE-----\n" +
                "MIIDqDCCApCgAwIBAgIGAYnorXvzMA0GCSqGSIb3DQEBCwUAMIGUMQswCQYDVQQGEwJVUzETMBEG A1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2FuIEZyYW5jaXNjbzENMAsGA1UECgwET2t0YTEU MBIGA1UECwwLU1NPUHJvdmlkZXIxFTATBgNVBAMMDGRldi00NTMzNTQxNzEcMBoGCSqGSIb3DQEJ ARYNaW5mb0Bva3RhLmNvbTAeFw0yMzA4MTIwNzM2MTBaFw0zMzA4MTIwNzM3MTBaMIGUMQswCQYD VQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2FuIEZyYW5jaXNjbzENMAsG A1UECgwET2t0YTEUMBIGA1UECwwLU1NPUHJvdmlkZXIxFTATBgNVBAMMDGRldi00NTMzNTQxNzEc MBoGCSqGSIb3DQEJARYNaW5mb0Bva3RhLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoC ggEBAOBfPdvayC25e9zjZU8tUjcQcn0H7d57kkraG2aQVw9WMPBwqgKU+0ThSgnf5sTXVBNGdctj 4A9hXkdj7Sdr3xB/dO9u+5hE7dBbz4MLGpbvtNtclLt0+q3z7hwYjUhowe7UKhxRso8bDXe+Q7HM FLDgANyo07rBDmgCGBsytB9BNBA5/FSvnAeOmaqu9fLcP9rpolK3gyMsPTakFcchVGPxAXRdCwVF AHctoixdjtNqUoajIPtmXSK8OLduc0a1mmipWExd8G9JKSfuUt0K1JpihOYPOtbgOH+1Kcjur8Rn hDYgY4iLFAkvCdPZ9SgqLvQ8mgof4dL6jyHBVgt5WVUCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEA 1N6USwAHlVBFNJR97y2ywAl2rdRBwfoQjvEtolt0wF68CQ84dr5ZBi2jal5nXFWOTqMNpGOQ16HH 0kWI+UbJ5HmjLGBAknSoVEm956JW6MdoR450uQUN9d2xfPGarr1OzBBLgvmVVFcKFfCRFPHVAI/i 2oZDdw2fgCtybwXgpZ2L+IC/kgiLLOIjOu3r1AxQYJX22PWn6WeztyHeJ8UgXi6Ok8Q7JLeRd+2c QLG+WCBEuDOlHBB3w+Yyf3d7dqsbnpQoZ+xmcUl6wAu2rHfjiwTa0zzk3ed79urV46SRrZPQ6qp1 yaxwgvSDjrFAyM2qGvSM3UvsaWZ3HtzhgP3C1g==\n" +
                "-----END CERTIFICATE-----").getBytes());
        final CertificateFactory cf = CertificateFactory.getInstance("X.509");
        final X509Certificate cert = (X509Certificate) cf.generateCertificate(is);


        final Saml2X509Credential credential = new Saml2X509Credential(cert,
                Saml2X509Credential.Saml2X509CredentialType.VERIFICATION, Saml2X509Credential.Saml2X509CredentialType.ENCRYPTION);
        return RelyingPartyRegistration.withRegistrationId("registrationId")
                .assertionConsumerServiceLocation("http://localhost:8080/login/saml2/sso/okta")
                .entityId("https://dev-45335417.okta.com/app/exkasd0wqaoSxRYYf5d7/sso/saml/metadata")
                .signingX509Credentials(c->c.add(credential))
                .build();
    }

    private Converter<OpenSaml4AuthenticationProvider.ResponseToken, ? extends AbstractAuthenticationToken> groupConverter() {
        var delegate = OpenSaml4AuthenticationProvider.createDefaultResponseAuthenticationConverter();
        return responseToken -> {
            Saml2Authentication authentication = delegate.convert(responseToken);
            Saml2AuthenticatedPrincipal principal = (Saml2AuthenticatedPrincipal) authentication.getPrincipal();
            List<String> groups = principal.getAttribute("groups");
            Set<GrantedAuthority> authoritySet = new HashSet<>();
            if (groups != null)
                groups.stream().map(SimpleGrantedAuthority::new).forEach(authoritySet::add);
            else
                authoritySet.addAll(authentication.getAuthorities());
            return new Saml2Authentication(principal, authentication.getSaml2Response(), authoritySet);
        };
    }

    private ClientRegistrationRepository azureClientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(Arrays.asList(ClientRegistration.withRegistrationId("azure")
                .clientId("c49a45a9-a944-498d-a9d2-c90303dab41b")
                .clientSecret("eDC8Q~CsGi5l03NKpCLNIyewW4AlooQ9CBeGUds8")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/azure/success")
                .scope("profile", "email", "openid")
                .authorizationUri("https://login.microsoftonline.com/3a83fefa-3473-4318-ac59-e6ffd43440b8/oauth2/v2.0/authorize")
                .tokenUri("https://login.microsoftonline.com/3a83fefa-3473-4318-ac59-e6ffd43440b8/oauth2/v2.0/token")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .build()));
    }


    private ClientRegistrationRepository oktaClientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(Arrays.asList(ClientRegistration.withRegistrationId("okta")
                .clientId("0oaaw0urqnXXiHATC5d7")
                .clientSecret("tChrHFZqBMhvNEVWxHl_0MTpDBejs6nS5kSXBxugALyHIal8esaHbxI4Is-Oa-LR")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/okta/success")
                .scope("profile", "email", "openid")
                .authorizationUri("https://dev-45335417.okta.com/oauth2/default/v1/authorize")
                .tokenUri("https://dev-45335417.okta.com/oauth2/default/v1/token")
                .userInfoUri("https://dev-45335417.okta.com/oauth2/default/v1/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://dev-45335417.okta.com/oauth2/default/v1/keys")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .build()));
    }

}

