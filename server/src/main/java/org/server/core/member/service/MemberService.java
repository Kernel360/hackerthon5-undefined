package org.server.core.member.service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.server.core.member.api.payload.response.LoginResponse;
import org.server.core.member.api.payload.response.OAuthTokenResponse;
import org.server.core.member.config.OAuthConfig;
import org.server.core.member.domain.Member;
import org.server.core.member.domain.MemberRepository;
import org.server.core.member.domain.OAuthProvider;
import org.server.core.member.domain.UserProfile;
import org.server.core.token.domain.Token;
import org.server.core.token.service.TokenService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final OAuthConfig oAuthConfig;
    private final TokenService tokenService;

    public UserProfile getUserProfile(String code, String provider) {
        OAuthProvider oAuthProvider = OAuthProvider.getProvider(provider);
        OAuthTokenResponse oAuthTokenResponse = getToken(code);

        return getUserProfile(oAuthProvider, oAuthTokenResponse);
    }

    @Transactional
    public Long getIdByUserProfileOrSave(UserProfile userProfile) {
        return getByAuthId(userProfile.id())
                .map(Member::getId)
                .orElseGet(() -> join(userProfile));
    }

    @Transactional
    public LoginResponse tryLogin(String code, String provider) {
        UserProfile userProfile = getUserProfile(code, provider);
        Long memberId = getIdByUserProfileOrSave(userProfile);
        Token token = tokenService.getOrGenerateToken(memberId, userProfile);

        return LoginResponse.builder()
                .tokenType("Bearer")
                .accessToken(token.getAccessToken())
                .build();
    }

    @Transactional
    public Long join(UserProfile userProfile) {
        return memberRepository.save(new Member(userProfile)).getId();
    }

    private OAuthTokenResponse getToken(String code) {
        return WebClient.create()
                .post()
                .uri(oAuthConfig.getTokenUri())
                .headers(header -> {
                    header.setBasicAuth(oAuthConfig.getClientId(), oAuthConfig.getSecretKey());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code))
                .retrieve()
                .bodyToMono(OAuthTokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> tokenRequest(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", oAuthConfig.getRedirectUri());
        return formData;
    }

    public UserProfile getUserProfile(OAuthProvider provider, OAuthTokenResponse tokenResponse) {
        Map<String, Object> attributes = getUserAttributes(tokenResponse);

        return new UserProfile(attributes.get("login").toString(), attributes.get("id").toString(),
                attributes.get("avatar_url").toString());
    }

    private Map<String, Object> getUserAttributes(OAuthTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(oAuthConfig.getUserInfoUri())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }

    private Optional<Member> getByAuthId(String oAuthId) {
        //FIXME: Optional 반환 X
        return memberRepository.findByAuthId(oAuthId);
    }
}
