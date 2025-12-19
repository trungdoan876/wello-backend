package com.wello.wellobackend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

/**
 * Service interface for verifying Google ID tokens from OAuth authentication
 */
public interface GoogleTokenVerifier {

    /**
     * Verify Google ID Token and return payload with user info
     * 
     * @param idTokenString ID Token from Google Sign-In (Flutter)
     * @return GoogleIdToken.Payload containing user info (email, name, picture,
     *         sub)
     * @throws Exception if token is invalid
     */
    GoogleIdToken.Payload verifyToken(String idTokenString) throws Exception;
}
