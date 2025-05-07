package org.server.core.site.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.server.global.common.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiteDomain extends BaseEntity {

    private String url;
    private String favicon;
    private String name;

    public SiteDomain(String url, String favicon, String name) {
        this.url = url;
        this.favicon = favicon;
        this.name = name;
    }

}
