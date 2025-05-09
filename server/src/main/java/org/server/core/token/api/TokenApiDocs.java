package org.server.core.token.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


public interface TokenApiDocs {

    @Operation(summary = "깃허브 로그인 요청 API", description = "회원가입 혹은 로그인을 요청하고, 토큰을 반환합니다")
    @ApiResponse(responseCode = "200", description = "로그인이 완료되었습니다.")
    String generateTokenForTest();
}
