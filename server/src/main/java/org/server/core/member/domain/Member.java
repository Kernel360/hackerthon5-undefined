package org.server.core.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.server.global.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Member extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @Enumerated(EnumType.STRING)
    private Position positon;

    private String profileUrl;

    private String nickname;

    public Member(OAuthProvider oAuthProvider, Position positon, String profileUrl, String nickname) {
        this.oAuthProvider = oAuthProvider;
        this.positon = positon;
        this.profileUrl = profileUrl;
        this.nickname = nickname;
    }
}
