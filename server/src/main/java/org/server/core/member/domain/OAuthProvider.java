package org.server.core.member.domain;

public enum OAuthProvider {
    GITHUB, KAKAO, GOOGLE;       //깃허브 로그인만 구현, 확장성 고려


    //FIXME: 임시 코드
    public static OAuthProvider getProvider(String requestUrl) {
        if (requestUrl.contains("github")) {
            return GITHUB;
        } else if (requestUrl.contains("kakao")) {
            return KAKAO;
        } else if (requestUrl.contains("google")) {
            return GOOGLE;
        }
        throw new IllegalArgumentException("[ERROR] 지원하지 않는 로그인 방식입니다.");
    }
}
