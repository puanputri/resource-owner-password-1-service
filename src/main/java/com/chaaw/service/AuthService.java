package com.chaaw.service;


import com.chaaw.dto.RefreshRequest;
import com.chaaw.repository.UserStore;
import io.quarkus.security.User;
import com.chaaw.dto.Credentials;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.smallrye.jwt.build.Jwt;


import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class AuthService {

    private static final long ACCESS_TOKEN_LIFESPAN = 300;
    private static final long REFRESH_TOKEN_LIFESPAN = 60 * 60 * 24 * 7;

    @Inject
    UserStore store;

    public Map<String, Object> login(Credentials cred) {
        User user = store.find(cred.username);
        if (user == null || !user.password.equals(cred.password)) {
            throw new SecurityException("Invalid credentials");
        }

        Instant now = Instant.now();

        String accessToken = Jwt.claims()
            .issuer("quarkus-app")
            .subject(user.username)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(ACCESS_TOKEN_LIFESPAN))
            .claim("email", user.email)
            .sign();

        String refreshToken = Jwt.claims()
            .issuer("quarkus-app")
            .subject(user.username)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(REFRESH_TOKEN_LIFESPAN))
            .claim("email", user.email)
            .claim("type", "refresh")
            .sign();

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("token_type", "Bearer");
        tokens.put("expires_in", ACCESS_TOKEN_LIFESPAN);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }

    public Map<String, Object> refresh(RefreshRequest req, JsonWebToken jwt) {
        if (!"refresh".equals(jwt.getClaim("type"))) {
            throw new SecurityException("Not a refresh token");
        }
        Instant now = Instant.now();
        String newAccess = Jwt.claims()
            .issuer("quarkus-app")
            .subject(jwt.getSubject())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(ACCESS_TOKEN_LIFESPAN))
            .claim("email", jwt.getClaim("email"))
            .sign();

        return Map.of(
            "access_token", newAccess,
            "token_type", "Bearer",
            "expires_in", ACCESS_TOKEN_LIFESPAN
        );
    }
}