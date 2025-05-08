package org.server.core.metric.domain;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findAllByMemberIdAndMetadataRequestAt(long memberId, Instant requestAt);
}
