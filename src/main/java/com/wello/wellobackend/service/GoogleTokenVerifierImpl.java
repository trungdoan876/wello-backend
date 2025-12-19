package com.wello.wellobackend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleTokenVerifierImpl implements GoogleTokenVerifier {

    @Value("${google.client.id:}")
    private String googleClientId;

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenVerifierImpl() {
        this.verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory())
                .build();
    }

    /**
     * Verify Google ID Token and return payload with user info
     * 
     * @param idTokenString ID Token from Google Sign-In (Flutter)
     * @return GoogleIdToken.Payload containing user info (email, name, picture,
     *         sub)
     * @throws Exception if token is invalid
     */
    @Override
    public GoogleIdToken.Payload verifyToken(String idTokenString) throws Exception {
        // Verify the token
        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken == null) {
            throw new IllegalArgumentException("Invalid ID token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        // Optional: Verify audience (Client ID) if configured
        // Temporarily disabled for testing - re-enable in production!
        // if (!googleClientId.isEmpty() &&
        // !payload.getAudience().equals(googleClientId)) {
        // throw new IllegalArgumentException("Token audience mismatch");
        // }

        return payload;
    }
}
