package org.server.core.metric.domain;

import org.server.core.site.domain.SiteDomain;

public record ActiveDomainEntry(
        SiteDomain siteDomain,
        long seconds
) {
}
