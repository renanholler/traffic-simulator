package com.simulator.util.Strategy;

import com.simulator.model.Celula;

public interface SyncStrategy {
    void acquire(Celula[] celulas) throws InterruptedException;
    void release(Celula[] celulas);
}
