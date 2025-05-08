package org.server.core.token.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.server.core.member.domain.Member;
import org.server.core.member.domain.UserProfile;
import org.server.core.member.service.MemberService;
import org.server.core.token.domain.Token;
import org.server.core.token.domain.TokenRepository;
import org.server.core.token.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private final static Long ACCESS_TOKEN_EXPIRE_TIME_MS = 604800000L; // 1주일

    @Transactional
    public Token getOrGenerateToken(Long memberId, UserProfile userProfile) {
        Optional<Token> maybeToken = getByMemberId(memberId);

        if (maybeToken.isPresent()) {
            return maybeToken.get();
        } else {
            return save(memberId, userProfile);
        }
    }

    private Optional<Token> getByMemberId(Long memberId) {
        //FIXME: Optional 반환X
        return tokenRepository.findByMemberId(memberId);
    }

    @Transactional
    public Token save(Long memberId, UserProfile userProfile) {

        Date expiredAt = calcExpiration();
        String accessToken = tokenProvider.createAccessToken(memberId, userProfile, secretKey, expiredAt);

        Token token = Token.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .expiredAt(expiredAt)
                .build();

        return tokenRepository.save(token);
    }

    public Date calcExpiration() {
        Date now = new Date();
        return new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME_MS);
    }
}
