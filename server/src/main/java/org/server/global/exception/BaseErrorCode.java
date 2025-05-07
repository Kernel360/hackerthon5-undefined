package org.server.global.exception;

public interface BaseErrorCode {
    public ErrorReason getErrorReason();
    String getExplainError() throws NoSuchFieldException;
}
