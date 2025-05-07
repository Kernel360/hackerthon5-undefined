package org.server.core.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.server.core.member.api.payload.request.MemberJoinRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Undefined 회원 기능 API", description = "회원 관련 기능 API 명세 문서입니다.")
public interface MemberApiDocs {

    @Operation(summary = "회원 가입 요청 API", description = "회원 가입을 요청합니다.")
    @ApiResponse(responseCode = "204")
    ResponseEntity<Void> join(@RequestBody MemberJoinRequest request);
}
