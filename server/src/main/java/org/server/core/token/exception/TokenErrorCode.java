package org.server.core.token.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.server.global.exception.BaseErrorCode;
import org.server.global.exception.ErrorReason;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TokenErrorCode implements BaseErrorCode {

    INVALID_TOKEN(HttpStatus.BAD_REQUEST.value(), "TOKEN_400_1", "비정상적인 토큰이 사용되었습니다.");

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
