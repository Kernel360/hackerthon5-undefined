package org.server.core.member.api;

import lombok.RequiredArgsConstructor;
import org.server.core.member.api.payload.request.MemberJoinRequest;
import org.server.core.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberRestController implements MemberApiDocs {

    private final MemberService memberService;

    @Override
    @PostMapping("/join")
    public ResponseEntity<Void> join(MemberJoinRequest request) {
        //memberService.join();         //TODO



        return ResponseEntity.noContent().build();
    }
}
