package org.server.core.metric.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.server.core.metric.api.payload.MetricGetRequest;
import org.server.core.metric.api.payload.MetricInsertRequest;
import org.server.core.metric.domain.ActiveTimeEntry;
import org.server.core.metric.service.MetricService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<ActiveTimeEntry>> get(@RequestBody MetricGetRequest request) {
        log.info("Get metric: {}", request);
        List<ActiveTimeEntry> activeTime = metricService.find(request.userId(), request.term());

        var response = ResponseEntity
                .status(HttpStatus.OK)
                .body(activeTime);

        return response;
    }
}
