package org.server.core.member.api.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.server.core.member.domain.Member;

@Getter
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String tokenType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginResponse(Long id, String name, String imageUrl, String tokenType,
                         String accessToken, String refreshToken) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public LoginResponse(Member member, String tokenType, String accessToken, String refreshToken) {
        this.id = member.getId();
        this.name = member.getAuthId();
        this.imageUrl = member.getProfileUrl();
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
