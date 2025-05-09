package org.server.core.metric.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.server.core.member.api.payload.response.LoginResponse;
import org.server.core.metric.api.payload.request.MetricDailyUsageRequest;
import org.server.core.metric.api.payload.request.MetricDailyUsageTraceRequest;
import org.server.core.metric.api.payload.request.MetricDomainDailyUsageRequest;
import org.server.core.metric.api.payload.request.MetricGetRequest;
import org.server.core.metric.api.payload.request.MetricInsertRequest;
import org.server.core.metric.api.payload.response.ActiveSiteDomainEntry;
import org.server.core.metric.domain.ActiveHourEntry;
import org.server.core.metric.domain.ActiveSiteDomainTraceEntry;
import org.server.core.metric.domain.ActiveTimeEntry;
import org.server.core.token.domain.LoginUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Metric 기능 API", description = "Metric 관련 기능 API 명세 문서입니다.")
public interface MetricApiDocs {

    @Operation(summary = "추적 데이터를 추가", description = "추적 데이터를 추가합니다.")
    @ApiResponse(responseCode = "204")
    void insert(@RequestBody @Schema(implementation = MetricInsertRequest.class) MetricInsertRequest request,
                @Parameter(hidden = true) LoginUser loginUser);

    @Operation(summary = "기간 단위 사용량 조회 요청 API", description = "선택된 기간동안의 사용량을 조회합니다")
    @ApiResponse(responseCode = "200",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ActiveTimeEntry.class))
            })
    ResponseEntity<List<ActiveTimeEntry>> get(
            @ModelAttribute @Schema(implementation = MetricGetRequest.class) MetricGetRequest request,
            @Parameter(hidden = true) LoginUser loginUser);


    @Operation(summary = "데일리 사용량 조회 요청 API", description = "24시간동안 사용한 내용을 조회합니다")
    @ApiResponse(responseCode = "200",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ActiveHourEntry.class))
            })
    ResponseEntity<List<ActiveHourEntry>> get(
            @ModelAttribute @Schema(implementation = MetricDailyUsageRequest.class) MetricDailyUsageRequest request,
            @Parameter(hidden = true) LoginUser loginUser);

    @Operation(summary = "통계 정보 요청 API", description = "그래프로 표현하기 위해 필요한 데이터를 조회합니다")
    @ApiResponse(responseCode = "200",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ActiveSiteDomainEntry.class))
            })
    ResponseEntity<List<ActiveSiteDomainEntry>> get(
            @ModelAttribute @Schema(implementation = MetricDomainDailyUsageRequest.class) MetricDomainDailyUsageRequest request,
            @Parameter(hidden = true) LoginUser loginUser);

    @Operation(summary = "24시간 동안 사용한 도메인 집계 API", description = "24시간동안 사용한 도메인을 집계합니다.")
    @ApiResponse(responseCode = "200",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ActiveSiteDomainTraceEntry.class))
            })
    ResponseEntity<List<ActiveSiteDomainTraceEntry>> get(
            @ModelAttribute @Schema(implementation = MetricDailyUsageTraceRequest.class) MetricDailyUsageTraceRequest request,
            @Parameter(hidden = true) LoginUser loginUser);
}
