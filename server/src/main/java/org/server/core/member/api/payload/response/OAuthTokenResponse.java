package org.server.core.member.api.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
}
