package org.server.core.token.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.server.core.token.domain.LoginUser;
import org.server.core.token.exception.TokenErrorCode;
import org.server.core.token.exception.TokenException;
import org.server.core.token.service.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private final JwtInterceptorHelper jwtInterceptorHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String accessToken = jwtInterceptorHelper.extractAccessTokenFromRequest(request);
            LoginUser loginUser = tokenService.getLoginUserFromAccessToken(accessToken);
            request.setAttribute("loginUser", loginUser);
        } catch (Exception e) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN);
        }
        return true;
    }
}
