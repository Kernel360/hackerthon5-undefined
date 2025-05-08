package org.server.core.metric.api.payload.request;

import org.server.core.metric.domain.Term;

public record MetricGetRequest(
        Long userId,
        Term term
) {
}
