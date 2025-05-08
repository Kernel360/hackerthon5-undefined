package org.server.core.token.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.server.core.member.domain.Member;
import org.server.core.member.domain.UserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    @Value("${jwt.token.secret}")
    private String secretKey;

    private static final Long ACCESS_TOKEN_EXPIRE_TIME_MS = 604800000L; // 1주일
    private static final String USER_NO_KEY_NAME = "USER_NO";
    private static final String USER_ID_KEY_NAME = "USER_ID";

    private SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    public Date calcExpiration() {
        Date now = new Date();
        return new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME_MS);
    }

    public String createAccessToken(Long memberId, UserProfile userProfile) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", memberId);
        claims.put("userProfile", userProfile);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(calcExpiration())
                .signWith(
                        Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS256)
                .compact();
    }
}
