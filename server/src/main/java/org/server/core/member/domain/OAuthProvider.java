package org.server.core.member.domain;

import org.server.core.member.exception.MemberErrorCode;
import org.server.core.member.exception.MemberException;

public enum OAuthProvider {
    GITHUB, KAKAO, GOOGLE;       //깃허브 로그인만 구현, 확장성 고려

    //FIXME: 임시 코드
    public static OAuthProvider getProvider(String requestUrl) {
        if (requestUrl.contains("github")) {
            System.out.println("깃허브");
            return GITHUB;
        } else if (requestUrl.contains("kakao")) {
            return KAKAO;
        } else if (requestUrl.contains("google")) {
            return GOOGLE;
        }
        throw new MemberException(MemberErrorCode.UNDEFINED_OAUTH_PROVIDER_ERROR);
    }
}
