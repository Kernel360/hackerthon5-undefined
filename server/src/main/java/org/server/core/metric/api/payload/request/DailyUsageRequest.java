package org.server.core.metric.api.payload.request;

import java.time.LocalDate;

public record DailyUsageRequest(
        Long userId,
        LocalDate date
) {

}
