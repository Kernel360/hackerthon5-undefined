package org.server.core.token.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.server.core.member.domain.UserProfile;

@Getter
@Builder
@ToString
public class LoginUser {
    private final long memberId;

    private final UserProfile userProfile;

    @Builder
    public LoginUser(long memberId, UserProfile userProfile) {
        this.memberId = memberId;
        this.userProfile = userProfile;
    }
}
