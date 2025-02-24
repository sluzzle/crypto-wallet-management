package com.example.cryptowalletmanagement.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for updating the prices of assets.
 */
@Component
public class UpdateTaskScheduler {

    private static final Logger logger = LoggerFactory.getLogger(UpdateTaskScheduler.class);

    private final SchedulingTask<?> schedulingTask;

    public UpdateTaskScheduler(SchedulingTask<?> schedulingTask) {
        this.schedulingTask = schedulingTask;
    }

    /**
     * schedules a task to execute periodically
     */
    @Scheduled(fixedDelayString = "${scheduler.price.update.interval}", initialDelay = 5000)
    @Transactional
    public void schedule() {
        try {
            logger.info("Scheduling task: {}" , schedulingTask.getClass().getSimpleName());
            schedulingTask.execute();
        } catch (RuntimeException e) {
            logger.error("Error during price update scheduler execution.", e);
        }
    }
}