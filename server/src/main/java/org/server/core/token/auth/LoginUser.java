package org.server.core.token.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class LoginUser {
    private final long userNo;

    private final String userId;

    @Builder
    public LoginUser(long userNo, String userId) {
        this.userNo = userNo;
        this.userId = userId;
    }
}
