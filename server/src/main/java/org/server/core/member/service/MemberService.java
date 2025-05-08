package org.server.core.member.service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.server.core.member.domain.UserProfile;
import org.server.core.member.api.payload.response.LoginResponse;
import org.server.core.member.api.payload.response.OAuthTokenResponse;
import org.server.core.member.config.OAuthConfig;
import org.server.core.member.domain.Member;
import org.server.core.member.domain.MemberRepository;
import org.server.core.member.domain.OAuthProvider;
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

    @Transactional
    public LoginResponse signUp(String providerName, String code) {

        OAuthProvider provider = OAuthProvider.getProvider(providerName);
        OAuthTokenResponse tokenResponse = getToken(code);
        UserProfile userProfile = getUserProfile(provider, tokenResponse);

        Optional<Member> maybeMember = getByAuthId(userProfile.id());

        Member member = maybeMember.orElseGet(() -> memberRepository.save(new Member(userProfile))); // 없으면 새로 생성 후 저장



        return LoginResponse.builder()
                .id(member.getId())
                .name(member.getNickname())
                .imageUrl(member.getProfileUrl())
                .tokenType("Bearer")
                .accessToken("access-token")        // FIXME
                .refreshToken("refresh-token")      // FIXME
                .build();
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
