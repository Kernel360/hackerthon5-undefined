package org.server.core.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.server.core.member.api.payload.request.MemberJoinRequest;
import org.server.core.member.api.payload.request.MemberUpdateRequest;
import org.server.core.member.api.payload.response.MemberProfileResponse;
import org.server.core.member.domain.Member;
import org.server.core.member.service.MemberService;
import org.server.core.metric.api.payload.request.MetricInsertRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Slf4j
public class MemberRestController implements MemberApiDocs {

    private final MemberService memberService;

    @Override
    @PostMapping("/join")
    public ResponseEntity<Void> join(MemberJoinRequest request) {
        //memberService.join();         //TODO

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원정보 조회", description = "현재 로그인한 회원의 프로필 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "회원정보 조회 성공")
    @GetMapping("/getProfile")
    public ResponseEntity<MemberProfileResponse> getProfile() {

        //임시 유저 아이디
        long memberId = 1;

        MemberProfileResponse profileResponse = memberService.getProfileInfo(memberId);         //TODO

        return ResponseEntity.status(HttpStatus.OK).body(profileResponse);
    }

    @Operation(summary = "회원정보 수정", description = "현재 로그인한 회원의 프로필 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "회원정보 수정 성공")
    @PutMapping ("/setProfile")
    public ResponseEntity<MemberProfileResponse> setProfile(@RequestBody MemberUpdateRequest request) {

        //임시 유저 아이디
        long memberId = 1;

        MemberProfileResponse profileResponse = memberService.setProfileInfo(memberId, request);         //TODO

        return ResponseEntity.status(HttpStatus.OK).body(profileResponse);
    }
}
