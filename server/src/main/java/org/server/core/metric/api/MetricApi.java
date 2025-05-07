package org.server.core.metric.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.server.core.metric.api.payload.MetricInsertRequest;
import org.server.core.metric.service.MetricService;
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

    @PostMapping
    public void insert(@RequestBody MetricInsertRequest request) {
        log.info("Insert metric: {}", request);
        metricService.save(request.userId(), request.toDomain(), request.toMetricMetadata());
    }
}
