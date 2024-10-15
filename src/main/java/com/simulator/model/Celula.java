package com.simulator.model;

public class Celula {

    private int tipo;
    private boolean ocupada;

    public Celula() {
        this.ocupada = false;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public synchronized boolean isOcupada() {
        return ocupada;
    }

    public synchronized void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }
}
