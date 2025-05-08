package org.server.core.metric.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findAllByMemberIdAndRequestAtAfter(long memberId, Instant requestAt);
}
