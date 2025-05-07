package org.server.core.member.service;

import lombok.RequiredArgsConstructor;
import org.server.core.member.domain.Member;
import org.server.core.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void save(Member member) {
        //FIXME : for test
        memberRepository.save(member);
    }
}
