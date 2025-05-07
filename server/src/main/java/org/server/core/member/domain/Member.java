package org.server.core.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.server.global.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
public class Member extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @Enumerated(EnumType.STRING)
    private Position position;

    private String profileUrl;

    private String nickname;

    public Member(OAuthProvider oAuthProvider, Position position, String profileUrl, String nickname) {
        this.oAuthProvider = oAuthProvider;
        this.position = position;
        this.profileUrl = profileUrl;
        this.nickname = nickname;
    }
}
