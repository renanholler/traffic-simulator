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
    private List<int[]> pontosEntrada;
    private List<int[]> pontosSaida;

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

        for (int i = 0; i < colunas; i++) {
            for (int j = 0; j < linhas; j++) {
                if (ehPontoEntrada(getLado(i, j), malha[i][j].getTipo())) {
                    pontosEntrada.add(new int[]{i, j});
                } else if(ehPontoSaida(getLado(i,j), malha[i][j].getTipo())) {
                    pontosSaida.add(new int[]{i,j});
                }
            }
        }
    }

    public int getLado(int x, int y) {
        if (x == colunas - 1) {
            return 2;
        }
        if (x == 0) {
            return 4;
        }
        if (y == linhas - 1) {
            return 3;
        }
        if (y == 0) {
            return 1;
        }
        return 0;
    }

    private boolean ehPontoEntrada(int lado, int tipoCelula) {
        if (lado == 1 && tipoCelula == 2) {
            return true;
        }
        if (lado == 2 && tipoCelula == 1) {
            return true;
        }
        if (lado == 3 && tipoCelula == 4) {
            return true;
        }
        return lado == 4 && tipoCelula == 3;
    }

    private boolean ehPontoSaida(int lado, int tipoCelula) {
        if (lado == 1 && tipoCelula == 4) {
            return true;
        }
        if (lado == 2 && tipoCelula == 3) {
            return true;
        }
        if (lado == 3 && tipoCelula == 2) {
            return true;
        }
        return lado == 4 && tipoCelula == 1;
    }

    public Celula[][] getMalha() {
        return malha;
    }

    public List<int[]> getPontosEntrada() {
        return pontosEntrada;
    }

    public List<int[]> getPontosSaida() {
        return pontosSaida;
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }
}
