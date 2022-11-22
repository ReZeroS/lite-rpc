package io.github.rezeros.core.common;

import lombok.Data;

import java.util.concurrent.Semaphore;

@Data
public class ServerServiceSemaphoreWrapper {

    private Semaphore semaphore;

    private int maxNums;

    public ServerServiceSemaphoreWrapper(int maxNums) {
        this.maxNums = maxNums;
        this.semaphore = new Semaphore(maxNums);
    }
}