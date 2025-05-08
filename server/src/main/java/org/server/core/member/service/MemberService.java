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
import org.server.core.member.exception.MemberErrorCode;
import org.server.core.member.exception.MemberException;
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

    public MemberProfileResponse getProfileInfo(Long memberId) {
        return MemberProfileResponse.from(getById(memberId));
    }

    @Transactional
    public MemberProfileResponse setProfileInfo(MemberUpdateRequest updateRequest, Long memberId) {
        Member member = getById(memberId);
        member.updateProfile(
                updateRequest.nickname(),
                updateRequest.position()
        );

        //TODO: SETTER 트랜잭션 자동 변경 적용 여부 확인 필요
        memberRepository.save(member);

        return MemberProfileResponse.from(member);
    }

    private Member getById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}