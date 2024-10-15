package com.simulator.util;

import com.simulator.util.Factory.ModelFactory;
import com.simulator.model.Malha;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class GridLoader {

    public static Malha loadGridFromFile(String filePath) throws IOException {
        // Carrega o arquivo da pasta resources/malhas
        InputStream inputStream = GridLoader.class.getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new IOException("Arquivo não encontrado: " + filePath);
        }

        // Lê o arquivo de malha
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> linhas = reader.lines().collect(Collectors.toList());

        // Primeira e segunda linha são as dimensões da malha
        int tamanhoX = Integer.parseInt(linhas.get(0).trim());
        int tamanhoY = Integer.parseInt(linhas.get(1).trim());

        Malha malha = ModelFactory.createMalha(tamanhoX, tamanhoY);

        // Preenche a matriz com os valores da malha a partir da terceira linha
        for (int i = 0; i < tamanhoX; i++) {
            // Divide a linha por qualquer número de espaços ou tabulações
            String[] linha = linhas.get(i + 2).trim().split("\\s+");
            for (int j = 0; j < tamanhoY; j++) {
                malha.setTipoCelula(i, j, Integer.parseInt(linha[j]));
            }
        }

        return malha; // Retorna a malha carregada
    }
}