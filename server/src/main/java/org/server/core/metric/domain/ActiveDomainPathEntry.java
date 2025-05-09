package org.server.core.metric.domain;

import org.server.core.site.domain.SiteDomain;

public record ActiveDomainPathEntry(
        SiteDomain siteDomain,
        String path,
        long duration
) {
}
