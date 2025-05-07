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

    @Column(nullable = false)
    private String url;
    private String favicon;
    private String name;

    public SiteDomain(String url){
        this(url, "", "");
    }

    public SiteDomain(String url, String favicon){
        this(url, favicon, "");
    }

    public SiteDomain(String url, String favicon, String name) {
        this.url = url;
        this.favicon = favicon;
        this.name = name;
    }

}
