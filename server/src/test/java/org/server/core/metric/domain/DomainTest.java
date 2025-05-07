package org.server.core.metric.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DomainTest {


    @Test
    void url에서_도메인만_추출합니다() {
        var domain = new Domain("https://www.naver.com");

        Assertions.assertThat(domain.getRoot()).isEqualTo("naver.com");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https",
            "",
            " ",
            "https://naver",
            "file://www.naver",
            "jdbc:mysql://"
    })
    void 유효하지_않은_도메인의_경우_예외가_발생합니다(String url) {
        Assertions.assertThatThrownBy(() -> new Domain(url))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
