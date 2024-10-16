package com.simuladormalha.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MalhaViaria {
    private Celula[][] malha;
    private int linhas;
    private int colunas;
    private List<Celula> pontosEntrada;
    private List<Celula> pontosSaida;

    public MalhaViaria(String caminhoArquivo) throws Exception {
        carregarMalha(caminhoArquivo);
        identificarPontosEntradaSaida();
    }

    private void carregarMalha(String caminhoArquivo) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(caminhoArquivo))));
        linhas = Integer.parseInt(br.readLine().trim());
        colunas = Integer.parseInt(br.readLine().trim());
        malha = new Celula[linhas][colunas];

        for (int i = 0; i < linhas; i++) {
            String[] valores = br.readLine().trim().split("\\s+");
            for (int j = 0; j < colunas; j++) {
                int tipo = Integer.parseInt(valores[j]);
                malha[i][j] = new Celula(tipo, i, j);
            }
        }
        br.close();
    }

    public void identificarPontosEntradaSaida() {
        pontosEntrada = new ArrayList<>();
        pontosSaida = new ArrayList<>();

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (ehPontoEntrada(getLado(i, j), malha[i][j].getTipo())) {
                    pontosEntrada.add(malha[i][j]);
                } else if(ehPontoSaida(getLado(i,j), malha[i][j].getTipo())) {
                    pontosSaida.add(malha[i][j]);
                }
            }
        }
    }

    public Direcao getLado(int linha, int coluna) {
        if (linha == linhas - 1) {
            return Direcao.BAIXO;
        }
        if (linha == 0) {
            return Direcao.CIMA;
        }
        if (coluna == colunas - 1) {
            return Direcao.DIREITA;
        }
        if (coluna == 0) {
            return Direcao.ESQUERDA;
        }
        return null;
    }

    private boolean ehPontoEntrada(Direcao lado, int tipoCelula) {
        if (lado == Direcao.ESQUERDA && tipoCelula == 2) {
            return true;
        }
        if (lado == Direcao.BAIXO && tipoCelula == 1) {
            return true;
        }
        if (lado == Direcao.DIREITA && tipoCelula == 4) {
            return true;
        }
        return lado == Direcao.CIMA && tipoCelula == 3;
    }

    private boolean ehPontoSaida(Direcao lado, int tipoCelula) {
        if (lado == Direcao.ESQUERDA && tipoCelula == 4) {
            return true;
        }
        if (lado == Direcao.BAIXO && tipoCelula == 3) {
            return true;
        }
        if (lado == Direcao.DIREITA && tipoCelula == 2) {
            return true;
        }
        return lado == Direcao.CIMA && tipoCelula == 1;
    }

    public Celula getCelula(int linha, int coluna) {
        return malha[linha][coluna];
    }

    public Celula[][] getMalha() {
        return malha;
    }

    public List<Celula> getPontosEntrada() {
        return pontosEntrada;
    }

    public List<Celula> getPontosSaida() {
        return pontosSaida;
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }
}
