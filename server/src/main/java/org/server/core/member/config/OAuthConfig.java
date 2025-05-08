package org.server.core.member.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "custom.oauth")
public class OAuthConfig {
    private String clientId;
    private String secretKey;
    private String redirectUri;
    private String tokenUri;
    private String userInfoUri;
}
