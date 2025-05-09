package org.server.core.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.server.global.exception.BaseErrorCode;
import org.server.global.exception.ErrorReason;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    UNDEFINED_OAUTH_PROVIDER_ERROR(HttpStatus.BAD_REQUEST.value(), "MEMBER_400_1", "올바른 로그인 방식이 아닙니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MEMBER_400_2", "멤버를 찾을 수 없습니다");

    private Integer status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder()
                .reason(reason)
                .code(code)
                .status(status)
                .build();
    }
}
