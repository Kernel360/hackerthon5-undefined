package org.server.core.member.api.payload.response;

import org.server.core.member.domain.Member;
import org.server.core.member.domain.Position;

public record MemberProfileResponse(
        Long id,
        String nickname,
        String profileUrl,
        Position position
) {
    public static MemberProfileResponse from(Member member) {
        return new MemberProfileResponse(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl(),
                member.getPosition()
        );
    }
}
