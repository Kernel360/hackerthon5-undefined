package org.server.core.token.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.server.core.member.domain.UserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    @Value("${jwt.token.secret}")
    private String secretKey;

    private static final Long ACCESS_TOKEN_EXPIRE_TIME_MS = 2592000000L; // 한 달
    private static final String MEMBER_PROFILE_KEY_NAME = "userProfile";
    private static final String MEMBER_ID_KEY_NAME = "userId";

    public Date calcExpiration() {
        Date now = new Date();
        return new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME_MS);
    }

    public String createAccessToken(Long memberId, UserProfile userProfile) {
        String token = Jwts.builder()
                .claim(MEMBER_ID_KEY_NAME, memberId)
                .claim(MEMBER_PROFILE_KEY_NAME, userProfile)
                .issuedAt(new Date())
                .expiration(calcExpiration())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS256)
                .compact();

        return token;
    }
}
