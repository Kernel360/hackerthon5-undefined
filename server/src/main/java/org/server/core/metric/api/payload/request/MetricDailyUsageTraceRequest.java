package org.server.core.metric.api.payload.request;

import java.time.LocalDate;

public record MetricDailyUsageTraceRequest(
        Long userId,
        LocalDate date
) {
    
}
