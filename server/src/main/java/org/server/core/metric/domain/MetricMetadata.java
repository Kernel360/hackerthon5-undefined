package org.server.core.metric.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.Instant;

@Embeddable
public record MetricMetadata(
        @Column(nullable = false)
        Instant requestAt,

        @Column(nullable = false, length = 1000)
        String path
) {
}
