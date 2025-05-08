package org.server.core.token.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.server.core.member.domain.UserProfile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final ObjectMapper objectMapper;

    public String createAccessToken(Long id, UserProfile userProfile, String secretKey, Date expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("userProfile", userProfile);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(
                        Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS256)
                .compact();
    }
}
