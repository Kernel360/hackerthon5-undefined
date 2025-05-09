package org.server.core.metric.api.payload.response;


import org.server.core.metric.domain.ActiveDomainEntry;

public record ActiveSiteDomainEntry(
        String name,
        String rootDomain,
        long seconds
) {
    public static ActiveSiteDomainEntry from(ActiveDomainEntry activeDomainEntry) {
        return new ActiveSiteDomainEntry(
                activeDomainEntry.siteDomain().getName(),
                activeDomainEntry.siteDomain().getRootDomain(),
                activeDomainEntry.seconds()
        );
    }
}
