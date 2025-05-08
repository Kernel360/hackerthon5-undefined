package org.server.core.site.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.server.global.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiteDomain extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String rootDomain;
    private String favicon;
    private String name;

    public SiteDomain(String rootDomain) {
        this(rootDomain, "", "");
    }

    public SiteDomain(String rootDomain, String favicon) {
        this(rootDomain, favicon, "");
    }

    public SiteDomain(String rootDomain, String favicon, String name) {
        this.rootDomain = rootDomain;
        this.favicon = favicon;
        this.name = name;
    }

}
