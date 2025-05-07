package org.server.core.member.exception;

import static org.server.global.consts.UndefinedStatics.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.server.global.exception.BaseErrorCode;
import org.server.global.exception.ErrorReason;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    UNDEFINED_OAUTH_PROVIDER_ERROR(BAD_REQUEST, "UNDEFINED_400_1", "올바른 로그인 방식이 아닙니다.");

    private Integer status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return null;
    }

    @Override
    public String getExplainError() throws NoSuchFieldException {
        return "";
    }
}
