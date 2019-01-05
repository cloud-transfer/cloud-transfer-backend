package com.github.cloud_transfer.cloud_transfer_backend.util.oauth;

import com.github.cloud_transfer.cloud_transfer_backend.exception.InvalidAuthenticationTokenException;
import com.github.cloud_transfer.cloud_transfer_backend.model.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleOauthLoginProvider implements OauthLoginProvider {

    private static final HttpTransport transport;
    private static final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    private static final GoogleIdTokenVerifier verifier;

    static {
        try {
            transport = GoogleNetHttpTransport.newTrustedTransport();
            verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(List.of("369031155877-hgs6so6m0u6ueki5p7u7d0mf92hvsiad.apps.googleusercontent.com"))
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public User getUser(String token) {
        try {
            GoogleIdToken idToken = verifier.verify(token);
            GoogleIdToken.Payload payload = idToken.getPayload();

            User user = new User();
            user.setEmail(payload.getEmail());
            user.setFirstName((String) payload.get("given_name"));
            user.setLastName((String) payload.get("family_name"));
            return user;

        } catch (GeneralSecurityException | IOException | NullPointerException e) {
            throw new InvalidAuthenticationTokenException(e);
        }
    }
}
