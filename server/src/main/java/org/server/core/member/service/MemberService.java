package org.server.core.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.server.core.member.api.payload.request.MemberUpdateRequest;
import org.server.core.member.domain.Member;
import org.server.core.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void save(Member member) {
        //FIXME : for test
        memberRepository.save(member);
    }

    public Member getProfileInfo(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return member;
    }

    @Transactional
    public Member setProfileInfo(long memberId, MemberUpdateRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        member.updateProfile(
                request.nickname(),
                request.position()
        );

        return memberRepository.save(member);
    }
}
