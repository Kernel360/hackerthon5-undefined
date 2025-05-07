package org.server.core.metric.service;

import lombok.RequiredArgsConstructor;
import org.server.core.metric.domain.Domain;
import org.server.core.metric.domain.Metric;
import org.server.core.metric.domain.MetricMetadata;
import org.server.core.metric.domain.MetricRepository;
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
        var siteDomain = siteDomainRepository.findByDomain(domain.getRoot())
                .orElseGet(() -> siteDomainRepository.save(externalSiteClient.getSiteDomain(domain)));

        metricRepository.save(new Metric(userId, siteDomain, metric));
    }
}
