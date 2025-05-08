package org.server.core.token.exception;

import org.server.global.exception.UpTimeException;

public class TokenException extends UpTimeException {
    public TokenException(TokenErrorCode errorCode) {
        super(errorCode);
    }
}
