package com.survey.global.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@SpringBootTest
public class RedisRepositoryTest extends AbstractContainerBaseTest{

    @Autowired
    UserCountRepository userCountRepository;

    @Test
    void incrementTest() {
        int N = 100;
        Long result = 0L;
        for(int i = 0; i<N; i++) {
            result = userCountRepository.increment("1");
        }
        assertThat(result)
                .isEqualTo(N);
    }
}
