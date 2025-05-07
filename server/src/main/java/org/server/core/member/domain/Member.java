package org.server.core.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.server.global.common.BaseEntity;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @Enumerated(EnumType.STRING)
    private Position positon;

    private String profileUrl;

    private String nickname;
}
