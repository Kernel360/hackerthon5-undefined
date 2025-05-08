package org.server.core.metric.api.payload.request;

import java.time.LocalDate;

public record MetricDomainDailyUsageRequest(
        Long userId,
        LocalDate date
) {
}
