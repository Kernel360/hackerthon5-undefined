package org.server.core.metric.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.server.core.metric.api.payload.request.MetricDailyUsageRequest;
import org.server.core.metric.api.payload.request.MetricDailyUsageTraceRequest;
import org.server.core.metric.api.payload.request.MetricDomainDailyUsageRequest;
import org.server.core.metric.api.payload.request.MetricGetRequest;
import org.server.core.metric.api.payload.request.MetricInsertRequest;
import org.server.core.metric.api.payload.response.ActiveSiteDomainEntry;
import org.server.core.metric.domain.ActiveHourEntry;
import org.server.core.metric.domain.ActiveSiteDomainTraceEntry;
import org.server.core.metric.domain.ActiveTimeEntry;
import org.server.core.metric.service.MetricService;
import org.server.core.metric.service.MetricUsageService;
import org.server.core.token.domain.LoginUser;
import org.server.core.token.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/metrics")
public class MetricApi {

    private final MetricService metricService;
    private final MetricUsageService metricUsageService;
    private final TokenService tokenService;

    @PostMapping
    public void insert(
            @RequestBody MetricInsertRequest request, LoginUser loginUser
    ) {
        metricService.save(loginUser.getMemberId(), request.toDomain(), request.toMetricMetadata());
    }

    @GetMapping
    public ResponseEntity<List<ActiveTimeEntry>> get(
            @ModelAttribute MetricGetRequest request,
            LoginUser loginUser
    ) {
        log.info("Get metric: {}", request);
        List<ActiveTimeEntry> activeTime = metricService.findByTerm(loginUser.getMemberId(), request.term());

        return ResponseEntity.status(HttpStatus.OK).body(activeTime);
    }


    @GetMapping("daily-usage")
    public ResponseEntity<List<ActiveHourEntry>> get(
            @ModelAttribute MetricDailyUsageRequest request,
            LoginUser loginUser
    ) {
        return ResponseEntity.ok(metricUsageService.aggregateByDailyUsage(loginUser.getMemberId(), request.date()));
    }

    @GetMapping("/daily-usage/domain-usage-ratio")
    public ResponseEntity<List<ActiveSiteDomainEntry>> get(
            @ModelAttribute MetricDomainDailyUsageRequest request,
            LoginUser loginUser
    ) {
        return ResponseEntity.ok(metricUsageService.aggregateByDomainDailyUsage(
                        loginUser.getMemberId(),
                        request.date()
                )
        );
    }

    @GetMapping("/daily-usage/trace")
    public ResponseEntity<List<ActiveSiteDomainTraceEntry>> get(
            @ModelAttribute MetricDailyUsageTraceRequest request,
            LoginUser loginUser
    ) {
        return ResponseEntity.ok(metricUsageService.aggregateByDailyUsageTrace(
                        loginUser.getMemberId(),
                        request.date()
                )
        );
    }
}
