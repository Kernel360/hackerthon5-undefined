package org.server.core.site.infra;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.server.core.site.domain.SiteDomain;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class ExternalSiteClientImpl implements ExternalSiteClient {

    private final RestClient restClient;

    public ExternalSiteClientImpl() {
        this.restClient = RestClient.create();
    }

    // TODO sub 도메인 추출까지 가능하도록 해야 함
    @Override
    public SiteDomain getSiteDomain(String url) {
        var html = getHtml(url);

        if (html == null) {
            return new SiteDomain(url);
        }

        var doc = Jsoup.parse(html, url);
        var siteTitle = doc.title();
        var faviconUrl = parseFaviconUrl(doc);

        return new SiteDomain(url, siteTitle, faviconUrl);
    }

    private String parseFaviconUrl(Document doc) {
        return Optional.ofNullable(doc.selectFirst("link[rel~=(?i)^(shortcut )?icon$]"))
                .map(faviconLink -> faviconLink.attr("abs:href"))
                .orElse("");
    }

    private String getHtml(String url) {
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }
}
