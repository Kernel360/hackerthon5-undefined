package org.server.core.site.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteDomainRepository extends JpaRepository<SiteDomain, Long> {

    Optional<SiteDomain> findByRootDomain(String domain);
}
