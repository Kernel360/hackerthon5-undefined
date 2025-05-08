package org.server.global.dummy;

import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.server.core.member.domain.Member;
import org.server.core.member.domain.MemberRepository;
import org.server.core.member.domain.OAuthProvider;
import org.server.core.member.domain.Position;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Profile({"local", "dev"})
public class DummyMemberGenerator {

    private final MemberRepository memberRepository;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> memberRepository.saveAll(
                IntStream.iterate(1, i -> i + 1)
                        .limit(10)
                        .mapToObj(i -> new Member(
                                "test"+i,
                                "https://www.naver.com",
                                "nickname" + i
                        ))
                        .toList()
        );
    }
}
