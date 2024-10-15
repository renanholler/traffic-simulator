package com.simulator.util.Strategy;


import com.simulator.model.Celula;

import java.util.concurrent.Semaphore;

public class SemaphoreStrategy implements SyncStrategy {
    private final Semaphore semaphore;

    public SemaphoreStrategy() {
        this.semaphore = new Semaphore(1);
    }

    @Override
    public void acquire(Celula[] celulas) throws InterruptedException {
        semaphore.acquire();
        for (Celula celula : celulas) {
            synchronized (celula) {
                while (celula.isOcupada()) {
                    celula.wait();
                }
                celula.setOcupada(true);
            }
        }
    }

    @Override
    public void release(Celula[] celulas) {
        for (Celula celula : celulas) {
            synchronized (celula) {
                celula.setOcupada(false);
                celula.notifyAll();
            }
        }
        semaphore.release();
    }
}
