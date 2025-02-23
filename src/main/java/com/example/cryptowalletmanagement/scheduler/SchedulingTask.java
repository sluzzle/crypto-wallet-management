package com.example.cryptowalletmanagement.scheduler;

/**
 * Represents a task to be scheduled
 */
public interface SchedulingTask<T> {
    T execute();
}
