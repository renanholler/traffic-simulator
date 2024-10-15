package com.simulator.model;

import com.simulator.util.Observer.Observer;
import com.simulator.util.Observer.Subject;
import com.simulator.util.Strategy.SyncStrategy;

import java.util.ArrayList;
import java.util.List;

public class Veiculo implements Runnable, Subject {

    private int velocidade;
    private int posicaoX;
    private int posicaoY;
    private Malha malha;
    private SyncStrategy syncStrategy;
    private boolean running = true;
    private List<Observer> observers;

    public Veiculo(int velocidade, Malha malha, SyncStrategy syncStrategy, int startX, int startY) {
        this.velocidade = velocidade;
        this.posicaoX = startX;
        this.posicaoY = startY;
        this.malha = malha;
        this.syncStrategy = syncStrategy;
        this.observers = new ArrayList<>();
    }

    @Override
    public void run() {
        while(true) {
            try{
                mover();
                Thread.sleep(velocidade);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void registraObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observers) {
            o.update(this);
        }
    }

    private void mover() throws InterruptedException {
        // Determinar a próxima posição com base no sentido da via
        int[] proximaPosicao = calcularProximaPosicao();
        int nextX = proximaPosicao[0];
        int nextY = proximaPosicao[1];

        // Obter as células envolvidas
        Celula celulaAtual = malha.getCelula(posicaoY, posicaoX);
        Celula celulaProxima = malha.getCelula(nextY, nextX);

        // Tentar adquirir as células necessárias
        syncStrategy.acquire(new Celula[]{celulaProxima});

        // Liberar a célula atual
        syncStrategy.release(new Celula[]{celulaAtual});

        // Atualizar a posição do veículo
        posicaoX = nextX;
        posicaoY = nextY;

        // Notificar os observadores (interface gráfica)
        notifyObservers();

        // Verificar se chegou ao destino
        if (chegouAoDestino()) {
            this.running = false;
            syncStrategy.release(new Celula[]{celulaProxima});
        }
    }

    private int[] calcularProximaPosicao() {
        // Implementar lógica para determinar a próxima posição
        // Exemplo simples: mover para a direita
        return new int[]{posicaoX, posicaoY - 1};
    }

    private boolean chegouAoDestino() {
        // Implementar lógica para verificar se o veículo chegou ao destino
        // Exemplo: se estiver na borda direita da malha
        return posicaoX >= malha.getLenX() + 1 || posicaoY <= malha.getLenY() - 1;
    }

    public int getPosicaoX() {
        return posicaoX;
    }

    public int getPosicaoY() {
        return posicaoY;
    }

}
