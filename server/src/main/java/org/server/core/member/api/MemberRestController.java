package org.server.core.member.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.server.core.member.api.payload.request.MemberJoinRequest;
import org.server.core.member.api.payload.response.LoginResponse;
import org.server.core.member.domain.OAuthProvider;
import org.server.core.member.service.MemberService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberRestController implements MemberApiDocs {

    private final MemberService memberService;

    @Override
    @PostMapping("/join")
    public ResponseEntity<Void> join(MemberJoinRequest request) {
        //memberService.join();         //TODO

        log.info("Join request : {}", request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/login/oauth")
    public ResponseEntity<LoginResponse> signUp(@RequestParam String code) {
        LoginResponse response = memberService.signUp("github", code);       //fixme: 하드코딩 X

        return ResponseEntity.ok().body(response);
    }
}
