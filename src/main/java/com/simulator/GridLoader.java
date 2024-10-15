package com.simulator;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class GridLoader {

    public int[][] loadGridFromFile(String filePath) throws IOException {
        // Carrega o arquivo da pasta resources/malhas
        InputStream inputStream = getClass().getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new IOException("Arquivo não encontrado: " + filePath);
        }

        // Lê o arquivo de malha
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> lines = reader.lines().collect(Collectors.toList());

        // Primeira e segunda linha são as dimensões da malha
        int rows = Integer.parseInt(lines.get(0).trim()); // Remove espaços extras
        int cols = Integer.parseInt(lines.get(1).trim()); // Remove espaços extras

        // Inicializa a matriz para armazenar a malha
        int[][] grid = new int[rows][cols];

        // Preenche a matriz com os valores da malha a partir da terceira linha
        for (int i = 0; i < rows; i++) {
            // Divide a linha por qualquer número de espaços ou tabulações
            String[] line = lines.get(i + 2).trim().split("\\s+");
            for (int j = 0; j < cols; j++) {
                grid[i][j] = Integer.parseInt(line[j]);
            }
        }

        return grid; // Retorna a malha carregada
    }
}