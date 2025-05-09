package org.server.core.metric.domain;

import org.server.core.site.domain.SiteDomain;

import java.time.LocalDateTime;
import java.util.List;

public record ActiveTimeEntry(SiteDomain siteDomain, List<PathDuration> pathDurations) {
    public static record PathDuration(String path, Long duration, LocalDateTime lastRequestAt) {
    }
}