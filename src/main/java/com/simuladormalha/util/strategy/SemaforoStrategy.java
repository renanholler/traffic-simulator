package com.simuladormalha.util.strategy;

import com.simuladormalha.model.Celula;
import com.simuladormalha.model.MalhaViaria;

import java.util.List;
import java.util.concurrent.Semaphore;

public class SemaforoStrategy implements ExclusaoMutuaStrategy {
    private Semaphore[][] semaforos;

    public SemaforoStrategy(MalhaViaria malha) {
        int linhas = malha.getLinhas();
        int colunas = malha.getColunas();
        semaforos = new Semaphore[linhas][colunas];

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                semaforos[i][j] = new Semaphore(1);
            }
        }
    }

    @Override
    public synchronized boolean isCaminhoLivre(List<Celula> caminho) {
        boolean livre = true;
        for (Celula celula : caminho) {
            if (celula.estaReservada() && semaforos[celula.getLinha()][celula.getColuna()].availablePermits() == 0) {
                livre = false;
            }
        }
        return livre;
    }

    @Override
    public synchronized boolean isOcupado(Celula celula) {
        return celula.estaOcupada() && semaforos[celula.getLinha()][celula.getColuna()].availablePermits() == 0;
    }

    @Override
    public synchronized boolean tentarReservar(List<Celula> caminho) {
        for (Celula celula : caminho) {
            try {
                if (!semaforos[celula.getLinha()][celula.getColuna()].tryAcquire()) {
                    liberarCaminho(caminho);
                    return false;
                }
            } catch (Exception e) {
                liberarCaminho(caminho);
                return false;
            }
        }
        return true;
    }

    @Override
    public synchronized void liberarCaminho(List<Celula> caminho) {
        for (Celula celula : caminho) {
            semaforos[celula.getLinha()][celula.getColuna()].release();
        }
    }
}
