package org.server.core.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.server.core.member.api.payload.request.MemberJoinRequest;
import org.server.core.member.api.payload.request.MemberUpdateRequest;
import org.server.core.member.api.payload.response.LoginResponse;
import org.server.core.member.api.payload.response.MemberProfileResponse;
import org.server.core.token.domain.LoginUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Undefined 회원 기능 API", description = "회원 관련 기능 API 명세 문서입니다.")
public interface MemberApiDocs {

    @Operation(summary = "깃허브 로그인 요청 API", description = "회원가입 혹은 로그인을 요청하고, 토큰을 반환합니다")
    @ApiResponse(responseCode = "200", description = "로그인이 완료되었습니다.",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
            })
    ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestParam String code);

    @Operation(summary = "프로필 정보 요청 API", description = "로그인한 회원의 프로필 정보를 받아옵니다")
    @ApiResponse(responseCode = "200", description = "프로필 정보를 받아왔습니다.",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MemberProfileResponse.class))
            })
    ResponseEntity<MemberProfileResponse> getProfile(@Parameter(hidden = true) LoginUser loginUser);

    @Operation(summary = "프로필 수정 요청 API", description = "회원의 프로필 수정을 요청합니다")
    @ApiResponse(responseCode = "200", description = "로그인이 완료되었습니다.",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
            })
    ResponseEntity<MemberProfileResponse> setProfile(
            @RequestBody @Schema(implementation = MemberUpdateRequest.class) MemberUpdateRequest memberUpdateRequest,
            @Parameter(hidden = true) LoginUser loginUser);
}
