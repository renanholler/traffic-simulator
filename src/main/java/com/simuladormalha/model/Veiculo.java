package com.simuladormalha.model;

import com.simuladormalha.util.strategy.ExclusaoMutuaStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Veiculo extends Thread {

    private final MalhaViaria malha;
    private int linhaAtual;
    private int colunaAtual;
    private final int velocidade;
    private final ExclusaoMutuaStrategy exclusaoMutua;
    private boolean ativo;

    public Veiculo(MalhaViaria malha, int linhaInicial, int colunaInicial, int velocidade, ExclusaoMutuaStrategy exclusaoMutua) {
        this.malha = malha;
        this.linhaAtual = linhaInicial;
        this.colunaAtual = colunaInicial;
        this.velocidade = velocidade;
        this.exclusaoMutua = exclusaoMutua;
        this.ativo = true;
    }

    @Override
    public void run() {
        try {
            while (ativo) {
                if(chegouAoDestino()) {
                    continue;
                }
                List<Celula> proximosMovimentos = escolherProximaPosicao();
                if (proximosMovimentos ==   null) {
                    Thread.sleep(velocidade);
                    continue;
                }

                if (exclusaoMutua.tentarReservar(proximosMovimentos)) {
                    moverVeiculo(proximosMovimentos);
                    exclusaoMutua.liberarCaminho(proximosMovimentos);
                }
                Thread.sleep(velocidade);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean chegouAoDestino() {
        if(malha.getPontosSaida().contains(malha.getCelula(linhaAtual,colunaAtual))) {
            desativar();
            return true;
        }
        return false;
    }

    private List<Celula> escolherProximaPosicao() {
        List<Celula> caminho = new ArrayList<>();
        List<List<Celula>> caminhos = new ArrayList<>();
        Random random = new Random();

        switch (malha.getCelula(linhaAtual, colunaAtual).getTipo()) {
            case 1:
                if (malha.getCelula(cima(1), colunaAtual).getTipo() == 1) {
                    caminho.add(malha.getCelula(cima(1), colunaAtual));
                    caminhos.add(caminho);
                    break;
                }
                switch (malha.getCelula(cima(1), colunaAtual).getTipo()) {
                    case 5:
                        caminho.add(malha.getCelula(cima(1), colunaAtual));
                        caminho.add(malha.getCelula(cima(2), colunaAtual));
                        caminho.add(malha.getCelula(cima(3), colunaAtual));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(cima(1), colunaAtual));
                        caminho.add(malha.getCelula(cima(2), colunaAtual));
                        caminho.add(malha.getCelula(cima(2), esquerda(1)));
                        caminho.add(malha.getCelula(cima(2), esquerda(2)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(cima(1), colunaAtual));
                        caminho.add(malha.getCelula(cima(2), colunaAtual));
                        caminho.add(malha.getCelula(cima(2), esquerda(1)));
                        caminho.add(malha.getCelula(cima(1), esquerda(1)));
                        caminho.add(malha.getCelula(linhaAtual , esquerda(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
                    case 9:
                        caminho.add(malha.getCelula(cima(1), colunaAtual));
                        caminho.add(malha.getCelula(cima(2), colunaAtual));
                        caminho.add(malha.getCelula(cima(3), colunaAtual));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(cima(1), colunaAtual));
                        caminho.add(malha.getCelula(cima(2), colunaAtual));
                        caminho.add(malha.getCelula(cima(2), esquerda(1)));
                        caminho.add(malha.getCelula(cima(2), esquerda(2)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(cima(1), colunaAtual));
                        caminho.add(malha.getCelula(cima(2), colunaAtual));
                        caminho.add(malha.getCelula(cima(2), esquerda(1)));
                        caminho.add(malha.getCelula(cima(1), esquerda(1)));
                        caminho.add(malha.getCelula(linhaAtual , esquerda(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(cima(1), colunaAtual));
                        caminho.add(malha.getCelula(cima(1), direita(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
                }
            case 2:
                if (malha.getCelula(linhaAtual, direita(1)).getTipo() == 2) {
                    caminho.add(malha.getCelula(linhaAtual, direita(1)));
                    caminhos.add(caminho);
                    break;
                }
                switch (malha.getCelula(linhaAtual, direita(1)).getTipo()) {
                    case 6:
                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
                        caminho.add(malha.getCelula(linhaAtual, direita(2)));
                        caminho.add(malha.getCelula(linhaAtual, direita(3)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
                        caminho.add(malha.getCelula(linhaAtual, direita(2)));
                        caminho.add(malha.getCelula(cima(1), direita(2)));
                        caminho.add(malha.getCelula(cima(2), direita(2)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
                        caminho.add(malha.getCelula(linhaAtual, direita(2)));
                        caminho.add(malha.getCelula(cima(1), direita(2)));
                        caminho.add(malha.getCelula(cima(1), direita(1)));
                        caminho.add(malha.getCelula(linhaAtual , direita(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
                    case 11:
                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
                        caminho.add(malha.getCelula(linhaAtual, direita(2)));
                        caminho.add(malha.getCelula(linhaAtual, direita(3)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
                        caminho.add(malha.getCelula(linhaAtual, direita(2)));
                        caminho.add(malha.getCelula(cima(1), direita(2)));
                        caminho.add(malha.getCelula(cima(2), direita(2)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
                        caminho.add(malha.getCelula(linhaAtual, direita(2)));
                        caminho.add(malha.getCelula(cima(1), direita(2)));
                        caminho.add(malha.getCelula(cima(1), direita(1)));
                        caminho.add(malha.getCelula(linhaAtual , direita(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
                        caminho.add(malha.getCelula(baixo(1), direita(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
                }
            case 3:
                if (malha.getCelula(baixo(1), colunaAtual).getTipo() == 3) {
                    caminho.add(malha.getCelula(baixo(1), colunaAtual));
                    caminhos.add(caminho);
                    break;
                }
                switch (malha.getCelula(baixo(1), colunaAtual).getTipo()) {
                    case 7:
                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
                        caminho.add(malha.getCelula(baixo(2), colunaAtual));
                        caminho.add(malha.getCelula(baixo(3), colunaAtual));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
                        caminho.add(malha.getCelula(baixo(2), colunaAtual));
                        caminho.add(malha.getCelula(baixo(2), direita(1)));
                        caminho.add(malha.getCelula(baixo(2), direita(2)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
                        caminho.add(malha.getCelula(baixo(2), colunaAtual));
                        caminho.add(malha.getCelula(baixo(2), direita(1)));
                        caminho.add(malha.getCelula(baixo(1), direita(1)));
                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
                    case 12:
                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
                        caminho.add(malha.getCelula(baixo(2), colunaAtual));
                        caminho.add(malha.getCelula(baixo(3), colunaAtual));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
                        caminho.add(malha.getCelula(baixo(2), colunaAtual));
                        caminho.add(malha.getCelula(baixo(2), direita(1)));
                        caminho.add(malha.getCelula(baixo(2), direita(2)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
                        caminho.add(malha.getCelula(baixo(2), colunaAtual));
                        caminho.add(malha.getCelula(baixo(2), direita(1)));
                        caminho.add(malha.getCelula(baixo(1), direita(1)));
                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
                        caminho.clear();
                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
                        caminho.add(malha.getCelula(baixo(1), esquerda(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
                }
            case 4:
                if (malha.getCelula(linhaAtual, esquerda(1)).getTipo() == 4) {
                    caminho.add(malha.getCelula(linhaAtual, esquerda(1)));
                    caminhos.add(caminho);
                    break;
                }
                switch (malha.getCelula(linhaAtual, esquerda(1)).getTipo()) {
                    case 8:
                        caminho.add(malha.getCelula(linhaAtual, esquerda(1)));
                        caminho.add(malha.getCelula(linhaAtual, esquerda(2)));
                        caminho.add(malha.getCelula(linhaAtual, esquerda(3)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(linhaAtual, esquerda(1)));
                        caminho.add(malha.getCelula(linhaAtual, esquerda(2)));
                        caminho.add(malha.getCelula(baixo(1), esquerda(2)));
                        caminho.add(malha.getCelula(baixo(2), esquerda(2)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(linhaAtual, esquerda(1)));
                        caminho.add(malha.getCelula(linhaAtual, esquerda(2)));
                        caminho.add(malha.getCelula(baixo(1), esquerda(2)));
                        caminho.add(malha.getCelula(baixo(1), esquerda(1)));
                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
                    case 10:
                        caminho.add(malha.getCelula(linhaAtual, esquerda(1)));
                        caminho.add(malha.getCelula(linhaAtual, esquerda(2)));
                        caminho.add(malha.getCelula(linhaAtual, esquerda(3)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(linhaAtual, esquerda(1)));
                        caminho.add(malha.getCelula(linhaAtual, esquerda(2)));
                        caminho.add(malha.getCelula(baixo(1), esquerda(2)));
                        caminho.add(malha.getCelula(baixo(2), esquerda(2)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(linhaAtual, esquerda(1)));
                        caminho.add(malha.getCelula(linhaAtual, esquerda(2)));
                        caminho.add(malha.getCelula(baixo(1), esquerda(2)));
                        caminho.add(malha.getCelula(baixo(1), esquerda(1)));
                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        caminho.add(malha.getCelula(linhaAtual, esquerda(1)));
                        caminho.add(malha.getCelula(cima(1), esquerda(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
                }
                break;
        }

        if (caminhos.isEmpty())
            return null;

        return caminhos.get(random.nextInt(caminhos.size()));
    }

    private int cima(int celulas) {
        return linhaAtual - celulas;
    }

    private int baixo(int celulas) {
        return linhaAtual + celulas;
    }

    private int direita(int celulas) {
        return colunaAtual + celulas;
    }

    private int esquerda(int celulas) {
        return colunaAtual - celulas;
    }

    private void moverVeiculo(List<Celula> caminho) {
        malha.getMalha()[linhaAtual][colunaAtual].setOcupada(false);
        for (Celula celula : caminho) {
            linhaAtual = celula.getLinha();
            colunaAtual = celula.getColuna();
            celula.setOcupada(true);
        }
    }

    public void desativar() {
        ativo = false;
        malha.getCelula(linhaAtual, colunaAtual).setOcupada(false);
        interrupt();
    }

    public int getLinhaAtual() {
        return linhaAtual;
    }

    public int getColunaAtual() {
        return colunaAtual;
    }

}
