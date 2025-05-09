package org.server.core.site.infra;

import org.server.core.metric.domain.Domain;
import org.server.core.site.domain.SiteDomain;

public interface ExternalSiteClient {
    SiteDomain getSiteDomain(Domain domain);
}
