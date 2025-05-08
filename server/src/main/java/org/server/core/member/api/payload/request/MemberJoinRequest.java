package org.server.core.member.api.payload.request;

import org.server.core.member.domain.OAuthProvider;

public record MemberJoinRequest(
        String nickname,
        String oAuthProvider,
        String profileUrl,
        String position
) {
    public MemberJoinRequest {
        oAuthProvider = OAuthProvider.getProvider(oAuthProvider).name(); // FIXME: 임시
    }
}
