package org.server.core.token.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.server.core.member.domain.UserProfile;
import org.server.core.token.config.JwtConfig;
import org.server.core.token.exception.TokenErrorCode;
import org.server.core.token.exception.TokenException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtConfig jwtConfig;
    private static final Long ACCESS_TOKEN_EXPIRE_TIME_MS = 2592000000L; // 한 달

    public Date calcExpiration() {
        Date now = new Date();
        return new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME_MS);
    }

    public String createAccessToken(Long memberId, UserProfile userProfile) {
        String token = Jwts.builder()
                .claim(jwtConfig.getMemberIdKey(), memberId)
                .claim(jwtConfig.getMemberProfileKey(), userProfile)
                .issuedAt(new Date())
                .expiration(calcExpiration())
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public Claims getClaims(String accessToken) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (Exception e) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN);
        }

        return claims;
    }
}
