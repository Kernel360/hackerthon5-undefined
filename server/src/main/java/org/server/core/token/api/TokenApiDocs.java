package org.server.core.token.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


public interface TokenApiDocs {

    @Operation(summary = "임시 토큰 발행 요청", description = "테스트를 위해 임시로 토큰을 가져옵니다")
    @ApiResponse(responseCode = "200", description = "토큰 발행이 완료되었습니다")
    String generateTokenForTest();
}
