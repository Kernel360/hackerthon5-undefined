package org.server.core.member.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Position {
    NONE("설정 전", 0),
    BACKEND("백엔드 개발자", 1),
    FRONTEND("프론트엔드 개발자", 2);

    private String name;
    private int sequence;
}
