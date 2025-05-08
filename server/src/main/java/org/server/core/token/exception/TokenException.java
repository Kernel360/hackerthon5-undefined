package org.server.core.token.exception;

import lombok.extern.slf4j.Slf4j;
import org.server.global.exception.UpTimeException;

@Slf4j
public class TokenException extends UpTimeException {
    public TokenException(TokenErrorCode errorCode) {
        super(errorCode);
        log.error("TokenErrorCode : {}", errorCode);
    }
}
