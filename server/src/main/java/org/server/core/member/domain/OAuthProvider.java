package org.server.core.member.domain;

import java.util.Arrays;
import org.server.core.member.exception.MemberErrorCode;
import org.server.core.member.exception.MemberException;

public enum OAuthProvider {
    GITHUB, KAKAO, GOOGLE;       //깃허브 로그인만 구현, 확장성 고려

    //FIXME: 임시 코드
    public static OAuthProvider getProvider(String requestUrl) {
        return Arrays.stream(OAuthProvider.values())
                .filter(provider -> requestUrl.contains(provider.name().toLowerCase())) //FIXME: LowerCase 보장?
                .findFirst()
                .orElseThrow(() -> new MemberException(MemberErrorCode.UNDEFINED_OAUTH_PROVIDER_ERROR));
    }
}
