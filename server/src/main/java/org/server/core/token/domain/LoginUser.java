package org.server.core.token.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.server.core.member.domain.UserProfile;

@Getter
@ToString
@Builder
public class LoginUser {

    @NonNull
    private long memberId;

    @NonNull
    private UserProfile userProfile;

    public LoginUser(long memberId, UserProfile userProfile) {
        this.memberId = memberId;
        this.userProfile = userProfile;
    }
}
