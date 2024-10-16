package com.simuladormalha.util.strategy;

import com.simuladormalha.model.Celula;

import java.util.List;

public class MonitorStrategy implements ExclusaoMutuaStrategy {

    @Override
    public synchronized boolean tentarReservar(List<Celula> caminho) {
        for (Celula celula : caminho) {
            if (celula.estaOcupada() || celula.estaReservada()) {
                liberarCaminho(caminho);
                return false;
            }
        }
        for (Celula celula : caminho) {
            celula.setReservada(true);
        }
        return true;
    }

    @Override
    public synchronized void liberarCaminho(List<Celula> caminho) {
        for (Celula celula : caminho) {
            celula.setReservada(false);
        }
    }
}
