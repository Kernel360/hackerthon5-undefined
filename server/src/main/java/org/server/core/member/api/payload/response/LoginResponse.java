package org.server.core.member.api.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.server.core.member.domain.Member;

@Getter
@NoArgsConstructor
public class LoginResponse {
    private String tokenType;
    private String accessToken;

    @Builder
    public LoginResponse(String tokenType, String accessToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }
}
