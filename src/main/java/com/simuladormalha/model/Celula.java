package com.simuladormalha.model;

public class Celula {

    private int tipo;
    private int linha;
    private int coluna;
    private boolean ocupada;
    private boolean reservada;

    public Celula(int tipo) {
        this.tipo = tipo;
        this.ocupada = false;
        this.reservada = false;
    }

    public synchronized boolean estaOcupada() {
        return ocupada;
    }

    public synchronized void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public synchronized boolean estaReservada() {
        return reservada;
    }

    public synchronized void setReservada(boolean reservada) {
        this.reservada = reservada;
    }

    public int getTipo() {
        return tipo;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }
}
