package com.example.cryptowalletmanagement.scheduler;

import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class UpdateTaskSchedulerTest {

    @MockitoSpyBean
    UpdateTaskScheduler task;

    @Test
    void reportCurrentTime() {
        await().atMost(Durations.TEN_SECONDS).untilAsserted(() -> {
            verify(task, atLeast(1)).schedule();
        });
    }
}
