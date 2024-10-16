package com.simulator.model;

import java.util.HashSet;
import java.util.Set;

public class Celula {

    private int tipo;
    private boolean ocupada;
    private Set<Direction> direcoesPermitidas;

    public Celula() {
        this.ocupada = false;
        this.direcoesPermitidas = new HashSet<>();
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
        definirDirecoesPermitidas();
    }

    public synchronized boolean isOcupada() {
        return ocupada;
    }

    public synchronized void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public Set<Direction> getDirecoesPermitidas() {
        return direcoesPermitidas;
    }

    private void definirDirecoesPermitidas() {
        direcoesPermitidas.clear();
        switch (tipo) {
            case 1: // Estrada Cima
                direcoesPermitidas.add(Direction.UP);
                break;
            case 2: // Estrada Direita
                direcoesPermitidas.add(Direction.RIGHT);
                break;
            case 3: // Estrada Baixo
                direcoesPermitidas.add(Direction.DOWN);
                break;
            case 4: // Estrada Esquerda
                direcoesPermitidas.add(Direction.LEFT);
                break;
            case 5: // Cruzamento Cima
                direcoesPermitidas.add(Direction.UP);
                break;
            case 6: // Cruzamento Direita
                direcoesPermitidas.add(Direction.RIGHT);
                break;
            case 7: // Cruzamento Baixo
                direcoesPermitidas.add(Direction.DOWN);
                break;
            case 8: // Cruzamento Esquerda
                direcoesPermitidas.add(Direction.LEFT);
                break;
            case 9: // Cruzamento Cima e Direita
                direcoesPermitidas.add(Direction.UP);
                direcoesPermitidas.add(Direction.RIGHT);
                break;
            case 10: // Cruzamento Cima e Esquerda
                direcoesPermitidas.add(Direction.UP);
                direcoesPermitidas.add(Direction.LEFT);
                break;
            case 11: // Cruzamento Direita e Baixo
                direcoesPermitidas.add(Direction.RIGHT);
                direcoesPermitidas.add(Direction.DOWN);
                break;
            case 12: // Cruzamento Baixo e Esquerda
                direcoesPermitidas.add(Direction.DOWN);
                direcoesPermitidas.add(Direction.LEFT);
                break;
            default:
                // Não permite movimento
                break;
        }
    }

    public boolean isCruzamento() {
        return tipo >= 5 && tipo <= 12;
    }
}
