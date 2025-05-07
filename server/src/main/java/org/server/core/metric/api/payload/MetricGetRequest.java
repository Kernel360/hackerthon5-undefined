package org.server.core.metric.api.payload;

public record MetricGetRequest(
        Long userId,
        String term
) {
}
