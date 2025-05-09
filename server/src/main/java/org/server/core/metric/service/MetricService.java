package org.server.core.metric.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.server.core.metric.domain.ActiveTimeEntry;
import org.server.core.metric.domain.Domain;
import org.server.core.metric.domain.Metric;
import org.server.core.metric.domain.MetricMetadata;
import org.server.core.metric.domain.MetricRepository;
import org.server.core.metric.domain.Term;
import org.server.core.site.domain.SiteDomain;
import org.server.core.site.domain.SiteDomainRepository;
import org.server.core.site.infra.ExternalSiteClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        List<Metric> userMetrics = metricRepository.findAllByMemberIdAndMetadataRequestAtAfter(
                userId,
                start.toInstant(ZoneOffset.of("+09:00"))
        );
        if (userMetrics.isEmpty()) {
            return Collections.emptyList();
        }

        return calculateActiveTime(userMetrics);
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
        List<ActiveTimeEntry> activeTimeEntries = new ArrayList<>();

        // 각 사이트 도메인의 path별로 요청 시간을 계산
        for (Metric metric : metrics) {
            SiteDomain siteDomain = metric.getSiteDomain();
            String path = metric.getMetadata().path();
            LocalDateTime requestAt = LocalDateTime.ofInstant(metric.getMetadata().requestAt(), ZoneOffset.of("+00:00"));

            ActiveTimeEntry existingEntry = activeTimeEntries.stream()
                    .filter(entry -> entry.siteDomain().equals(siteDomain))
                    .findFirst()
                    .orElse(null);

            // 해당 사이트 도메인이 없으면 새로 추가
            // 해당 사이트 도메인이 있으면 pathDurations를 가져옴
            if (existingEntry == null) {
                List<ActiveTimeEntry.PathDuration> pathDurations = new ArrayList<>();
                pathDurations.add(new ActiveTimeEntry.PathDuration(path, 0L, requestAt));
                activeTimeEntries.add(new ActiveTimeEntry(siteDomain, pathDurations));
            } else {
                List<ActiveTimeEntry.PathDuration> pathDurations = existingEntry.pathDurations();
                ActiveTimeEntry.PathDuration existingPath = pathDurations.stream()
                        .filter(pathDuration -> pathDuration.path().equals(path))
                        .findFirst()
                        .orElse(null);

                // 해당 path가 없으면 새로 추가
                // 해당 path가 있으면 duration을 계산
                if (existingPath == null) {
                    pathDurations.add(new ActiveTimeEntry.PathDuration(path, 0L, requestAt));
                } else {
                    // 요청 시간 계산
                    Duration duration = Duration.between(existingPath.lastRequestAt(), requestAt);
                    long totalDuration = existingPath.duration() + duration.toSeconds();
                    pathDurations.remove(existingPath);
                    pathDurations.add(new ActiveTimeEntry.PathDuration(path, totalDuration, requestAt));
                }
            }
        }

    return activeTimeEntries;
    }
}
