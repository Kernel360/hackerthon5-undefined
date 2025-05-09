package org.server.core.member.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {

    @Value("${custom.oauth.tokenUri}")
    private String tokenUri;

    @Bean("githubClient")
    public GithubApiHttpInterface githubApiHttpInterface() {
        RestClient githubClient = RestClient.builder()
                .baseUrl(tokenUri)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(githubClient))
                .build();

        return factory.createClient(GithubApiHttpInterface.class);
    }
}
