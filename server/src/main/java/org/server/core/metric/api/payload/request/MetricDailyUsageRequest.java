package org.server.core.metric.api.payload.request;

import java.time.LocalDate;

public record MetricDailyUsageRequest(
        Long userId,
        LocalDate date
) {

}
