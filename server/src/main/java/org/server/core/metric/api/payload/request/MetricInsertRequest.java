package org.server.core.metric.api.payload.request;

import java.time.Instant;
import org.server.core.metric.domain.Domain;
import org.server.core.metric.domain.MetricMetadata;

public record MetricInsertRequest(
        Long userId,
        String url,
        Instant now
) {

    public Domain toDomain() {
        return new Domain(url);
    }

    public MetricMetadata toMetricMetadata() {
        return new MetricMetadata(now, url.replaceFirst("^https?://[^/]+", ""));
    }
}
