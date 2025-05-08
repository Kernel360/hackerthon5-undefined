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
@NoArgsConstructor
public class Member extends BaseEntity {

    private String authId;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @Enumerated(EnumType.STRING)
    private Position position;

    private String profileUrl;

    private String nickname;

    public Member(String oAuthId, String profileUrl, String nickname) {
        this.authId = oAuthId;
        this.oAuthProvider = OAuthProvider.GITHUB;  //FIXME: GITHUB 고정 X
        this.position = Position.NONE;
        this.profileUrl = profileUrl;
        this.nickname = nickname;
    }

    public Member(UserProfile userProfile) {
        this.authId = userProfile.id();
        this.oAuthProvider = OAuthProvider.GITHUB; //FIXME: GITHUB 고정 X
        this.position = Position.NONE;
        this.profileUrl = userProfile.avatarUrl();
        this.nickname = userProfile.name();
    }
}
