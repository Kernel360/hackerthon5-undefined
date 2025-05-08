package org.server.core.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.server.core.member.api.payload.request.MemberJoinRequest;
import org.server.core.member.api.payload.request.MemberUpdateRequest;
import org.server.core.member.api.payload.response.MemberProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Undefined 회원 기능 API", description = "회원 관련 기능 API 명세 문서입니다.")
public interface MemberApiDocs {

    @Operation(summary = "회원 가입 요청 API", description = "회원 가입을 요청합니다.")
    @ApiResponse(responseCode = "204")
    ResponseEntity<Void> join(@RequestBody MemberJoinRequest request);

//    @Operation(summary = "회원정보 조회", description = "현재 로그인한 회원의 프로필 정보를 조회합니다.")
//    @ApiResponse(responseCode = "200", description = "회원정보 조회 성공")
//    ResponseEntity<MemberProfileResponse> getProfile();
//
//    @Operation(summary = "회원정보 수정", description = "현재 로그인한 회원의 프로필 정보를 수정합니다.")
//    @ApiResponse(responseCode = "200", description = "회원정보 수정 성공")
//    ResponseEntity<MemberProfileResponse> setProfile(@RequestBody MemberUpdateRequest request);
}
