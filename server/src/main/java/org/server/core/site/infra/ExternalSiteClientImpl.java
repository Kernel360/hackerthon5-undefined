package org.server.core.site.infra;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.server.core.metric.domain.Domain;
import org.server.core.site.domain.SiteDomain;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
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
    public SiteDomain getSiteDomain(Domain domain) {
        var rootDomain = domain.getRoot();
        var html = getHtml("https://" + domain.getHost());

        if (html == null) {
            return new SiteDomain(rootDomain);
        }

        var doc = Jsoup.parse(html, rootDomain);
        var siteTitle = doc.title();
        var faviconUrl = parseFaviconUrl(doc, domain.getHost());

        return new SiteDomain(rootDomain, faviconUrl, siteTitle);
    }

    private String parseFaviconUrl(Document doc, String rootDomain) {
        return doc.select("link[rel~=(?i)^(icon|shortcut icon|apple-touch-icon)$]")
                .stream()
                .map(faviconLink -> faviconLink.attr("abs:href"))
                .filter(Strings::isNotBlank)
                .findFirst()
                .orElse("https://" + rootDomain + "/favicon.ico");
    }

    private String getHtml(String url) {
        try {
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException exception) {
            return exception.getResponseBodyAs(String.class);
        }
    }
}
