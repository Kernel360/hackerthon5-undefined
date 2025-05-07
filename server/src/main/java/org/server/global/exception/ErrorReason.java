package org.server.global.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
public record ErrorReason(
        Integer status,
        String code,
        String reason
) {
}
