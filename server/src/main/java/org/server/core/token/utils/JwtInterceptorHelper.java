package org.server.core.token.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.server.core.token.exception.TokenErrorCode;
import org.server.core.token.exception.TokenException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtInterceptorHelper {

    public String extractAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        throw new TokenException(TokenErrorCode.INVALID_TOKEN);
    }
}
