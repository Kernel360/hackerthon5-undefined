package org.server.core.member.service;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.server.core.member.api.payload.response.LoginResponse;
import org.server.core.member.api.payload.response.OAuthTokenResponse;
import org.server.core.member.config.GithubApiHttpInterface;
import org.server.core.member.config.OAuthConfig;
import lombok.extern.slf4j.Slf4j;
import org.server.core.member.api.payload.request.MemberUpdateRequest;
import org.server.core.member.api.payload.response.MemberProfileResponse;
import org.server.core.member.domain.Member;
import org.server.core.member.domain.MemberRepository;
import org.server.core.member.domain.OAuthProvider;
import org.server.core.member.domain.UserProfile;
import org.server.core.token.domain.Token;
import org.server.core.token.service.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final GithubApiHttpInterface githubApiHttpInterface;
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
        return githubApiHttpInterface.callTokenApi(oAuthConfig.getClientId(), oAuthConfig.getSecretKey(), code,
                oAuthConfig.getRedirectUri());
    }

    public UserProfile getUserProfile(OAuthProvider provider, OAuthTokenResponse tokenResponse) {
        Map<String, Object> attributes = getUserAttributes(tokenResponse);

        return new UserProfile(attributes.get("login").toString(), attributes.get("id").toString(),
                attributes.get("avatar_url").toString());
    }

    public Map<String, Object> getUserAttributes(OAuthTokenResponse tokenResponse) {
        String authorizationHeader = "Bearer " + tokenResponse.getAccessToken();
        return githubApiHttpInterface.callUserInfoApi(authorizationHeader);
    }

    private Optional<Member> getByAuthId(String oAuthId) {
        return memberRepository.findByAuthId(oAuthId);
    }

    public MemberProfileResponse getProfileInfo(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return MemberProfileResponse.from(member);
    }

    @Transactional
    public MemberProfileResponse setProfileInfo(long memberId, MemberUpdateRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        member.updateProfile(
                request.nickname(),
                request.position()
        );

        memberRepository.save(member);

        Member memberUpdate = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return MemberProfileResponse.from(memberUpdate);
    }
}