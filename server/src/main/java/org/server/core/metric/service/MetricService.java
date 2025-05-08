package org.server.core.metric.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.server.core.metric.domain.*;
import org.server.core.site.domain.SiteDomain;
import org.server.core.site.domain.SiteDomainRepository;
import org.server.core.site.infra.ExternalSiteClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.Duration;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MetricService {

    private final MetricRepository metricRepository;
    private final SiteDomainRepository siteDomainRepository;
    private final ExternalSiteClient externalSiteClient;

    public void save(Long userId, Domain domain, MetricMetadata metric) {
        var siteDomain = siteDomainRepository.findByRootDomain(domain.getRoot())
                .orElseGet(() -> siteDomainRepository.save(externalSiteClient.getSiteDomain(domain)));

        metricRepository.save(new Metric(userId, siteDomain, metric));
    }

    public List<ActiveTimeEntry> findByTerm(Long userId, Term term) {
        LocalDateTime start = getStartTime(term);

        List<Metric> userMetrics = metricRepository.findAllByMemberIdAndRequestAtAfter(userId, start.toInstant(ZoneOffset.of("+09:00")));
        if (userMetrics.isEmpty()) {
            return Collections.emptyList();
        }

        List<ActiveTimeEntry> activeTime = calculateActiveTime(userMetrics);

        return activeTime;
    }

    public LocalDateTime getStartTime(Term term) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = switch (term) {
            case DAY -> // 지난 24시간이 아닌 당일 0시부터
                    now.toLocalDate().atStartOfDay();
            case WEEK -> now.minusWeeks(1);
            case MONTH -> now.minusMonths(1);
            default -> throw new IllegalArgumentException("Invalid time parameter : " + term);
        };

        return start;
    }

    public List<ActiveTimeEntry> calculateActiveTime(List<Metric> metrics) {
        Map<SiteDomain, DomainTimeEntry> domainTime = new HashMap<>();

        // 각 사이트 도메인별로 요청 시간을 계산
        // path에 따라 요청 시간 계산 로직 추가 필요
        for (Metric metric : metrics) {
            SiteDomain siteDomain = metric.getSiteDomain();
            LocalDateTime requestAt = LocalDateTime.ofInstant(metric.getMetadata().requestAt(), ZoneOffset.of("+09:00"));

            domainTime.putIfAbsent(siteDomain, new DomainTimeEntry(requestAt, 0L));

            long duration = Duration.between(domainTime.get(siteDomain).getRequestAt(), requestAt).toSeconds();
            domainTime.get(siteDomain).addDuration(duration);
            domainTime.get(siteDomain).setRequestAt(requestAt);
        }

        List<ActiveTimeEntry> activeTime = new ArrayList<>();
        for (Map.Entry<SiteDomain, DomainTimeEntry> entry : domainTime.entrySet()) {
            activeTime.add(new ActiveTimeEntry(entry.getKey(), entry.getValue().getDuration()));
        }

        return activeTime;
    }

    @Getter
    @NoArgsConstructor
    private static class DomainTimeEntry {
        private LocalDateTime requestAt;
        private long duration;

        public DomainTimeEntry(LocalDateTime requestAt, long duration) {
            this.requestAt = requestAt;
            this.duration = duration;
        }

        public void setRequestAt(LocalDateTime requestAt) {
            this.requestAt = requestAt;
        }

        public void addDuration(long duration) {
            this.duration += duration;
        }
    }
}
