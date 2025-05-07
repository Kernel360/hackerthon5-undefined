package org.server.global.exception;

import lombok.Builder;

@Builder
public record ErrorReason(
        Integer status,
        String code,
        String reason
) {
}
