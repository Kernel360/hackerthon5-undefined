package org.server.core.metric.domain;

import com.google.common.net.InternetDomainName;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.Getter;

@Getter
public class Domain {

    private final String originUrl;
    private final String root;

    public Domain(String originUrl) {
        this.originUrl = originUrl;
        this.root = parseRootDomain(originUrl);
    }

    public static String parseRootDomain(String url) {
        try {
            var uri = new URI(url);
            String host = uri.getHost();

            if (host == null) {
                throw new IllegalArgumentException();
            }

            InternetDomainName internetDomainName = InternetDomainName.from(host).topPrivateDomain();
            return internetDomainName.toString();
        } catch (URISyntaxException | IllegalStateException e) {
            throw new IllegalArgumentException();
        }

    }

}
