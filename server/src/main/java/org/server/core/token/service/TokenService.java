package org.server.core.token.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.server.core.member.domain.UserProfile;
import org.server.core.token.domain.LoginUser;
import org.server.core.token.config.JwtConfig;
import org.server.core.token.domain.Token;
import org.server.core.token.domain.TokenRepository;
import org.server.core.token.exception.TokenErrorCode;
import org.server.core.token.exception.TokenException;
import org.server.core.token.utils.TokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

    private final JwtConfig jwtConfig;
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

    public LoginUser getLoginUserFromAccessToken(String accessToken) {
        Claims claims = tokenProvider.getClaims(accessToken);

        ObjectMapper objectMapper = new ObjectMapper();
        UserProfile userProfile = objectMapper.convertValue(
                claims.get(jwtConfig.getMemberProfileKey()), UserProfile.class
        );

        return LoginUser.builder()
                .memberId(claims.get(jwtConfig.getMemberIdKey(), Long.class))
                .userProfile(userProfile)
                .build();
    }

    public String substringToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN);
        }

        return authHeader.substring(7);
    }
}
