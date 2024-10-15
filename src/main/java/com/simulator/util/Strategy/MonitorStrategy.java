package com.simulator.util.Strategy;

import com.simulator.model.Celula;

public class MonitorStrategy implements SyncStrategy {

    @Override
    public void acquire(Celula[] celulas) throws InterruptedException {
        synchronized (this) {
            for (Celula celula : celulas) {
                synchronized (celula) {
                    while (celula.isOcupada()) {
                        celula.wait();
                    }
                    celula.setOcupada(true);
                }
            }
        }
    }

    @Override
    public void release(Celula[] celulas) {
        synchronized (this) {
            for (Celula celula : celulas) {
                synchronized (celula) {
                    celula.setOcupada(false);
                    celula.notifyAll();
                }
            }
        }
    }
}
