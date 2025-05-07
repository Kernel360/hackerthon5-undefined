package org.server.core.member.domain;

import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        //ID는 JPA가 자동 생성
        testMember = Member.builder()
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .oAuthProvider(OAuthProvider.GITHUB)
                .positon(Position.BACKEND)
                .profileUrl("s3 url")
                .nickname("testMember")
                .build();
    }

    @Test
    @DisplayName("회원 객체 저장 테스트")
    void t001() {
        //given
        memberRepository.save(testMember);

        //when
        Optional<Member> maybeMember = memberRepository.findById(1L);

        //then
        Assertions.assertThat(maybeMember.isPresent()).isTrue();
        Assertions.assertThat(maybeMember.get().getNickname()).isEqualTo("testMember");
    }
}