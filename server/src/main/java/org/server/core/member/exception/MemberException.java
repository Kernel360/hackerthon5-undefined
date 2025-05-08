package org.server.core.member.exception;

import org.server.global.exception.UndefinedException;

public class MemberException extends UndefinedException {

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }
}
