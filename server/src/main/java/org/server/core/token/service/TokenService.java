package org.server.core.token.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.server.core.member.domain.Member;
import org.server.core.member.domain.UserProfile;
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
        
        String accessToken = tokenProvider.createAccessToken(memberId, userProfile);

        Token token = Token.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .expiredAt(tokenProvider.calcExpiration())      //FIXME: 책임 넘겨줬으니 어떻게 받아올건지 고민
                .build();

        return tokenRepository.save(token);
    }

    public Member getMemberFromAccessToken(String accessToken) {
//        Claims claims = Jwts.parser()
//                .verifyWith(key)
//                .build()
//                .parseSignedClaims(accessToken)
//                .getPayload();


        //TODO: 목표 : 액세스 토큰으로 기반으로 회원을 받아온다
        return null;

//        return LoginUser.builder()
//                .userNo(claims.get(USER_NO_KEY_NAME, Long.class))
//                .userId(claims.get(USER_ID_KEY_NAME, String.class))
//                .build();
    }

}
