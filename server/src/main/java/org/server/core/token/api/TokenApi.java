package org.server.core.token.api;

import lombok.RequiredArgsConstructor;
import org.server.core.member.domain.UserProfile;
import org.server.core.token.utils.TokenProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenApi implements TokenApiDocs{

    private final TokenProvider tokenProvider;

    @Override
    @GetMapping("/gen/test")
    public String generateTokenForTest() {
        return tokenProvider.createAccessToken(1L, new UserProfile("TEST_ADMIN", "TEST_ADMIN_AUTH_ID", "URL_SRC"));
    }
}
