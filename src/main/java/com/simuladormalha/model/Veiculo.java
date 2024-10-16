package com.simuladormalha.model;


import com.simuladormalha.util.strategy.ExclusaoMutuaStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Veiculo extends Thread {

    private MalhaViaria malha;
    private int linhaAtual;
    private int colunaAtual;
    private int velocidade;
    private ExclusaoMutuaStrategy exclusaoMutua;
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
                List<int[]> proximosMovimentos = escolherProximaPosicao();
                if (proximosMovimentos == null) {
                    Thread.sleep(velocidade);
                    continue;
                }

                List<Celula> caminho = obterCaminho(proximosMovimentos);

                if (exclusaoMutua.tentarReservar(caminho)) {
                    moverVeiculo(caminho);
                    exclusaoMutua.liberarCaminho(caminho);
                }
                Thread.sleep(velocidade);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private List<int[]> escolherProximaPosicao() {
        // Implementar lógica para escolher a próxima posição com base no tipo da célula atual
        int tipo = malha.getMalha()[linhaAtual][colunaAtual].getTipo();
        List<int[]> movimentosPossiveis = new ArrayList<>();
        Random random = new Random();

        switch (tipo) {
            case 1, 5:
                movimentosPossiveis.add(new int[]{linhaAtual - 1, colunaAtual});
                break;
            case 2, 6:
                movimentosPossiveis.add(new int[]{linhaAtual, colunaAtual + 1});
                break;
            case 3, 7:
                movimentosPossiveis.add(new int[]{linhaAtual + 1, colunaAtual});
                break;
            case 4, 8:
                movimentosPossiveis.add(new int[]{linhaAtual, colunaAtual - 1});
                break;
            case 9:
                movimentosPossiveis.add(new int[]{linhaAtual - 1, colunaAtual});
                movimentosPossiveis.add(new int[]{linhaAtual, colunaAtual + 1});
                break;
            case 10:
                movimentosPossiveis.add(new int[]{linhaAtual - 1, colunaAtual});
                movimentosPossiveis.add(new int[]{linhaAtual, colunaAtual - 1});
                break;
            case 11:
                movimentosPossiveis.add(new int[]{linhaAtual + 1, colunaAtual});
                movimentosPossiveis.add(new int[]{linhaAtual, colunaAtual + 1});
                break;
            case 12:
                movimentosPossiveis.add(new int[]{linhaAtual + 1, colunaAtual});
                movimentosPossiveis.add(new int[]{linhaAtual, colunaAtual - 1});
                break;
        }

        if (movimentosPossiveis.isEmpty())
            return null;

        int[] proximaPosicao = movimentosPossiveis.get(random.nextInt(movimentosPossiveis.size()));
        List<int[]> caminho = new ArrayList<>();
        caminho.add(proximaPosicao);

        return caminho;
    }

    private List<Celula> obterCaminho(List<int[]> movimentos) {
        List<Celula> caminho = new ArrayList<>();
        for (int[] pos : movimentos) {
            caminho.add(malha.getMalha()[pos[0]][pos[1]]);
        }
        return caminho;
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
        interrupt();
    }

    public int getLinhaAtual() {
        return linhaAtual;
    }

    public int getColunaAtual() {
        return colunaAtual;
    }

}
