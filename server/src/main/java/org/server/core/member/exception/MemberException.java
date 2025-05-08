package org.server.core.member.exception;

import org.server.global.exception.UpTimeException;

public class MemberException extends UpTimeException {
    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }
}
