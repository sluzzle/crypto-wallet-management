package com.example.cryptowalletmanagement.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for updating the prices of assets.
 */
@Service
public class TaskScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TaskScheduler.class);

    private final SchedulingTask<?> schedulingTask;

    public TaskScheduler(SchedulingTask<?> schedulingTask) {
        this.schedulingTask = schedulingTask;
    }

    /**
     * schedules a task to execute periodically
     */
    @Scheduled(fixedDelayString = "${scheduler.price.update.interval}", initialDelay = 5000)
    @Transactional
    public void schedule() {
        try {
            logger.info("Starting the task scheduler");
            schedulingTask.execute();
        } catch (RuntimeException e) {
            logger.error("Error during price update scheduler execution.", e);
        }
    }
}