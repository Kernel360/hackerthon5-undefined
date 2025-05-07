package org.server.core.metric.domain;

import org.server.core.site.domain.SiteDomain;

public class ActiveTimeEntry {
    private final SiteDomain siteDomain;
    private final long duration;

    public ActiveTimeEntry(SiteDomain siteDomain, long duration) {
        this.siteDomain = siteDomain;
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }
}
