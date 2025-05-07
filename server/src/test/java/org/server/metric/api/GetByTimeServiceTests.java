package org.server.metric.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetByTimeServiceTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Test getByTime API")
    public void test() throws Exception {
        // Given
        String time = "day";
        Integer id = 1;

        // When
        ResponseEntity<Object[]> response = restTemplate.getForEntity(
                "/api/get-by-time?time=" + time + "&id=" + id,
                Object[].class
        );

        // Then
        assert response.getStatusCode().is2xxSuccessful();
        assertThat(response.getBody()).containsExactly(time, id);
    }

}
