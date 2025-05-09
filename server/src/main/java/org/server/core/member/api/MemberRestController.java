package org.server.core.member.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.server.core.member.api.payload.request.MemberJoinRequest;
import org.server.core.member.api.payload.request.MemberUpdateRequest;
import org.server.core.member.api.payload.response.LoginResponse;
import org.server.core.member.api.payload.response.MemberProfileResponse;
import org.server.core.member.service.MemberService;
import org.server.core.token.domain.LoginUser;
import org.server.core.token.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;
    private final TokenService tokenService;

    @GetMapping("/login/oauth/{provider}")
    public ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestParam String code) {
        LoginResponse response = memberService.tryLogin(code, provider);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/getProfile")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<MemberProfileResponse> getProfile(HttpServletRequest request) {
        String accessToken = tokenService.substringToken(request);
        LoginUser loginUser = tokenService.getLoginUserFromAccessToken(accessToken);

        MemberProfileResponse profileResponse = memberService.getProfileInfo(loginUser.getMemberId());
        return ResponseEntity.status(HttpStatus.OK).body(profileResponse);
    }

    @PutMapping("/setProfile")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<MemberProfileResponse> setProfile(@RequestBody MemberUpdateRequest memberUpdateRequest,
                                                            HttpServletRequest request) {
        String accessToken = tokenService.substringToken(request);
        LoginUser loginUser = tokenService.getLoginUserFromAccessToken(accessToken);


        MemberProfileResponse response = memberService.setProfileInfo(memberUpdateRequest, loginUser.getMemberId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
