package com.srecon.authenticator.ldap;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Hashtable;

public class AzureADLDAPAuthentication {

    public static void main(String[] args) throws KeyStoreException {
        Hashtable<String, String> env = createEnvironment();

//        System.setProperty("javax.net.ssl.trustStore", "<Path to trust store>");
//        System.setProperty("javax.net.ssl.trustStorePassword", "<trust store password>");

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream fis = new FileInputStream("<Path to the trustStore>")) {
            trustStore.load(fis, "<Your password>".toCharArray());
            DirContext ctx = new InitialDirContext(env);
            System.out.println("LDAP Authentication successful");

            // Perform additional operations using the authenticated context

            ctx.close();
        } catch (NamingException e) {
            System.err.println("LDAP Authentication failed: " + e.getMessage());
        } catch (NoSuchAlgorithmException | IOException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    private static Hashtable<String, String> createEnvironment() {
        String ldapURL = "ldap://your-azure-ad-domain-service-domain-name";
        String bindDN = "CN=UserName,CN=Users,DC=your-domain,DC=onmicrosoft,DC=com";
        String password = "YourUserPassword";

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapURL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, bindDN);
        env.put(Context.SECURITY_CREDENTIALS, password);
        return env;
    }
}
