package org.server.core.member.config;

import java.util.Map;
import org.server.core.member.api.payload.response.OAuthTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@Component
@HttpExchange
public interface GithubApiHttpInterface {

    @PostExchange(url = "https://github.com/login/oauth/access_token",
            contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            accept = MediaType.APPLICATION_JSON_VALUE)
    OAuthTokenResponse callTokenApi(@RequestParam("client_id") String clientId,
                                    @RequestParam("client_secret") String clientSecret,
                                    @RequestParam("code") String code,
                                    @RequestParam("redirect_uri") String redirectUri);

    @GetExchange("https://api.github.com/user")
    Map<String, Object> callUserInfoApi(@RequestHeader("Authorization") String accessToken);
}