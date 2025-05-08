package org.server.core.member.api.payload.request;

import org.server.core.member.domain.OAuthProvider;
import org.server.core.member.domain.Position;

public record MemberJoinRequest(
        String nickname,
        OAuthProvider oAuthProvider,
        String profileUrl,
        Position position
) {
}
