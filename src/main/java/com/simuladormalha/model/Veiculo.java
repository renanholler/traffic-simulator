package com.simuladormalha.model;

import com.simuladormalha.util.strategy.ExclusaoMutuaStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Veiculo extends Thread {

    private final MalhaViaria malha;
    private int linhaAtual;
    private int colunaAtual;
    private final int velocidade;
    private final ExclusaoMutuaStrategy exclusaoMutua;
    private boolean ativo;
    private List<Celula> rotaCruzamento = new ArrayList<>();

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
                Celula proximoMovimento = escolherProximaPosicao();
                if (proximoMovimento == null) {
                    Thread.sleep(velocidade);
                    continue;
                }

                moverVeiculo(proximoMovimento);
//                if (exclusaoMutua.tentarReservar(new ArrayList<>(proximoMovimento))) {
//                    exclusaoMutua.liberarCaminho(proximoMovimento);
//                }
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

    private Celula escolherProximaPosicao() {
        if(!rotaCruzamento.isEmpty()) {
            return rotaCruzamento.removeFirst();
        }

        int nextLinha = linhaAtual + deltaLinha(getDirecaoAtual());
        int nextColuna = colunaAtual + deltaColuna(getDirecaoAtual());

        if(isCruzamento(nextLinha, nextColuna)) {
            List<Celula> caminho = getRotaRandomica();
            while(caminho.getLast().getTipo() == 0) {
                caminho = getRotaRandomica();
            }
            if(this.exclusaoMutua.isCaminhoLivre(caminho) && exclusaoMutua.tentarReservar(caminho)) {
                rotaCruzamento = caminho;
                assert rotaCruzamento != null;
                return rotaCruzamento.removeFirst();
            } else {
                return null;
            }
        }
        Celula proximaPosicao = malha.getCelula(nextLinha, nextColuna);

        if(!(proximaPosicao.estaReservada() || proximaPosicao.estaOcupada()) && exclusaoMutua.tentarReservar(List.of(proximaPosicao))) {
            return proximaPosicao;
        }

        return null;
    }

    private int deltaLinha(Direcao direcaoAtual) {
        if(direcaoAtual.equals(Direcao.BAIXO)) {
            return 1;
        } else if (direcaoAtual.equals(Direcao.CIMA)) {
            return -1;
        }
        return 0;
    }

    private int deltaColuna(Direcao direcaoAtual) {
        if(direcaoAtual.equals(Direcao.DIREITA)) {
            return 1;
        } else if (direcaoAtual.equals(Direcao.ESQUERDA)){
            return -1;
        }
         return 0;
    }

    private Direcao getDirecaoAtual() {
        return switch (malha.getCelula(linhaAtual, colunaAtual).getTipo()) {
            case 1 -> Direcao.CIMA;
            case 2 -> Direcao.DIREITA;
            case 3 -> Direcao.BAIXO;
            case 4 -> Direcao.ESQUERDA;
            default -> null;
        };
    }

    private List<Celula> getRotaRandomica() {
        List<Celula> caminho = new ArrayList<>();
        List<List<Celula>> caminhos = new ArrayList<>();
        Random random = new Random();

        switch (malha.getCelula(linhaAtual, colunaAtual).getTipo()) {
            case 1:
                switch (malha.getCelula(cima(1), colunaAtual).getTipo()) {
                    case 1, 2, 3, 4:
                        caminho.add(malha.getCelula(cima(1), colunaAtual));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
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
//                        caminho.add(malha.getCelula(cima(1), colunaAtual));
//                        caminho.add(malha.getCelula(cima(2), colunaAtual));
//                        caminho.add(malha.getCelula(cima(2), esquerda(1)));
//                        caminho.add(malha.getCelula(cima(1), esquerda(1)));
//                        caminho.add(malha.getCelula(linhaAtual , esquerda(1)));
//                        caminhos.add(new ArrayList<>(caminho));
//                        caminho.clear();
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
//                        caminho.add(malha.getCelula(cima(1), colunaAtual));
//                        caminho.add(malha.getCelula(cima(2), colunaAtual));
//                        caminho.add(malha.getCelula(cima(2), esquerda(1)));
//                        caminho.add(malha.getCelula(cima(1), esquerda(1)));
//                        caminho.add(malha.getCelula(linhaAtual , esquerda(1)));
//                        caminhos.add(new ArrayList<>(caminho));
//                        caminho.clear();
                        caminho.add(malha.getCelula(cima(1), colunaAtual));
                        caminho.add(malha.getCelula(cima(1), direita(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
                }
                break;
            case 2:
                switch (malha.getCelula(linhaAtual, direita(1)).getTipo()) {
                    case 1, 2, 3, 4:
                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
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
//                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
//                        caminho.add(malha.getCelula(linhaAtual, direita(2)));
//                        caminho.add(malha.getCelula(cima(1), direita(2)));
//                        caminho.add(malha.getCelula(cima(1), direita(1)));
//                        caminho.add(malha.getCelula(cima(1) , colunaAtual));
//                        caminhos.add(new ArrayList<>(caminho));
//                        caminho.clear();
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
//                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
//                        caminho.add(malha.getCelula(linhaAtual, direita(2)));
//                        caminho.add(malha.getCelula(cima(1), direita(2)));
//                        caminho.add(malha.getCelula(cima(1), direita(1)));
//                        caminho.add(malha.getCelula(cima(1) , colunaAtual));
//                        caminhos.add(new ArrayList<>(caminho));
//                        caminho.clear();
                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
                        caminho.add(malha.getCelula(baixo(1), direita(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
                }
                break;
            case 3:
                switch (malha.getCelula(baixo(1), colunaAtual).getTipo()) {
                    case 1, 2, 3, 4:
                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
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
//                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
//                        caminho.add(malha.getCelula(baixo(2), colunaAtual));
//                        caminho.add(malha.getCelula(baixo(2), direita(1)));
//                        caminho.add(malha.getCelula(baixo(1), direita(1)));
//                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
//                        caminhos.add(new ArrayList<>(caminho));
//                        caminho.clear();
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
//                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
//                        caminho.add(malha.getCelula(baixo(2), colunaAtual));
//                        caminho.add(malha.getCelula(baixo(2), direita(1)));
//                        caminho.add(malha.getCelula(baixo(1), direita(1)));
//                        caminho.add(malha.getCelula(linhaAtual, direita(1)));
//                        caminhos.add(new ArrayList<>(caminho));
//                        caminho.clear();
                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
                        caminho.add(malha.getCelula(baixo(1), esquerda(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
                }
                break;
            case 4:
                switch (malha.getCelula(linhaAtual, esquerda(1)).getTipo()) {
                    case 1, 2, 3, 4:
                        caminho.add(malha.getCelula(linhaAtual, esquerda(1)));
                        caminhos.add(new ArrayList<>(caminho));
                        caminho.clear();
                        break;
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
//                        caminho.add(malha.getCelula(linhaAtual, esquerda(1)));
//                        caminho.add(malha.getCelula(linhaAtual, esquerda(2)));
//                        caminho.add(malha.getCelula(baixo(1), esquerda(2)));
//                        caminho.add(malha.getCelula(baixo(1), esquerda(1)));
//                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
//                        caminhos.add(new ArrayList<>(caminho));
//                        caminho.clear();
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
//                        caminho.add(malha.getCelula(linhaAtual, esquerda(1)));
//                        caminho.add(malha.getCelula(linhaAtual, esquerda(2)));
//                        caminho.add(malha.getCelula(baixo(1), esquerda(2)));
//                        caminho.add(malha.getCelula(baixo(1), esquerda(1)));
//                        caminho.add(malha.getCelula(baixo(1), colunaAtual));
//                        caminhos.add(new ArrayList<>(caminho));
//                        caminho.clear();
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

    private void moverVeiculo(Celula celula) {
        malha.getMalha()[linhaAtual][colunaAtual].setOcupada(false);
        exclusaoMutua.liberarCaminho(List.of(malha.getCelula(linhaAtual, colunaAtual)));
        linhaAtual = celula.getLinha();
        colunaAtual = celula.getColuna();
        celula.setOcupada(true);
    }

    public void desativar() {
        ativo = false;
        malha.getCelula(linhaAtual, colunaAtual).setOcupada(false);
        malha.getCelula(linhaAtual, colunaAtual).setReservada(false);
        interrupt();
    }

    public int getLinhaAtual() {
        return linhaAtual;
    }

    public int getColunaAtual() {
        return colunaAtual;
    }

    private boolean isCruzamento(int linha, int coluna) {
        return malha.getCelula(linha,coluna).getTipo() >= 5;
    }

}
