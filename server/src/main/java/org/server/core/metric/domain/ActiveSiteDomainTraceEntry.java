package org.server.core.metric.domain;

import java.util.List;
import java.util.Map.Entry;
import org.server.core.site.domain.SiteDomain;

public record ActiveSiteDomainTraceEntry(
        String rootDomain,
        String favicon,
        String name,
        List<PathDuration> pathDurations
) {

    public static ActiveSiteDomainTraceEntry from(Entry<SiteDomain, List<ActiveDomainPathEntry>> siteDomainListEntry) {
        var siteDomain = siteDomainListEntry.getKey();
        var activeDomainPathEntries = siteDomainListEntry.getValue();

        return new ActiveSiteDomainTraceEntry(
                siteDomain.getRootDomain(),
                siteDomain.getFavicon(),
                siteDomain.getName(),
                activeDomainPathEntries.stream()
                        .map(activeDomainPath -> new PathDuration(activeDomainPath.path(), activeDomainPath.duration()))
                        .toList()
        );
    }

    public record PathDuration(String path, Long duration) {
    }

}
