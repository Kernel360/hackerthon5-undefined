package org.server.core.token.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt.token")
public class JwtConfig {
    private String secretKey;
    private String memberIdKey;
    private String memberProfileKey;
}
