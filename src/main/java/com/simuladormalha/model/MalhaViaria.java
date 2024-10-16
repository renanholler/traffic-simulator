package com.simuladormalha.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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
        BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));
        linhas = Integer.parseInt(br.readLine());
        colunas = Integer.parseInt(br.readLine());
        malha = new Celula[linhas][colunas];

        for (int i = 0; i < linhas; i++) {
            String[] valores = br.readLine().trim().split("\\s+");
            for (int j = 0; j < colunas; j++) {
                int tipo = Integer.parseInt(valores[j]);
                malha[i][j] = new Celula(tipo);
            }
        }
        br.close();
    }

    private void identificarPontosEntradaSaida() {
        pontosEntrada = new ArrayList<>();
        pontosSaida = new ArrayList<>();

        for (int i = 0; i < linhas; i++) {
            if (malha[i][0].getTipo() != 0)
                pontosEntrada.add(new int[]{i, 0});
            if (malha[i][colunas - 1].getTipo() != 0)
                pontosSaida.add(new int[]{i, colunas - 1});
        }

        for (int j = 0; j < colunas; j++) {
            if (malha[0][j].getTipo() != 0)
                pontosEntrada.add(new int[]{0, j});
            if (malha[linhas - 1][j].getTipo() != 0)
                pontosSaida.add(new int[]{linhas - 1, j});
        }
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
