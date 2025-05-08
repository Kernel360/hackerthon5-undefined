package org.server.core.member.api.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthTokenResponse {

    @JsonProperty("access_token")
    String accessToken;
    String scope;
    @JsonProperty("token_type")
    String tokenType;

    @Builder
    public OAuthTokenResponse(String accessToken, String scope, String tokenType) {
        this.accessToken = accessToken;
        this.scope = scope;
        this.tokenType = tokenType;
    }
}
