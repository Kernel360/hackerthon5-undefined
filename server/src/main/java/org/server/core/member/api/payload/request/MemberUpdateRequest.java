package org.server.core.member.api.payload.request;

import org.server.core.member.domain.Position;

public record MemberUpdateRequest(
        String nickname,
        Position position
) {
}
