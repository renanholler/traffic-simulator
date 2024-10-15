package com.simulator;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.IOException;

public class TrafficSimulator extends Application {

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane(); // Contêiner para a malha viária
        GridLoader loader = new GridLoader();

        try {
            int[][] grid = loader.loadGridFromFile("/malhas/malha-exemplo-3.txt");
            double cellSize = 30;
            Color[][] colorGrid = new Color[grid.length][grid[0].length]; // Para manter as cores atribuídas

            // Adiciona números nas colunas (0 a n-1)
            for (int col = 0; col < grid[0].length; col++) {
                Label colLabel = new Label(String.valueOf(col));
                GridPane.setHalignment(colLabel, HPos.CENTER); // Centraliza o label
                gridPane.add(colLabel, col + 1, 0); // Ajusta para ter espaço à esquerda
            }

            // Adiciona números nas linhas (0 a n-1)
            for (int row = 0; row < grid.length; row++) {
                Label rowLabel = new Label(String.valueOf(row));
                gridPane.add(rowLabel, 0, row + 1); // Ajusta para ter espaço acima
            }

            // Percorre o grid para identificar estradas e cruzamentos
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (isRoad(grid[i][j])) {
                        // Se a estrada começa em uma borda ou sai de um cruzamento
                        Color roadColor = getColorForDirection(grid[i][j]);
                        propagateRoad(grid, colorGrid, i, j, roadColor, grid[i][j]); // Passa a cor e direção inicial
                    } else if (isCrossroad(grid[i][j])) {
                        // Pinta cruzamento de cinza claro
                        colorGrid[i][j] = Color.LIGHTGRAY;
                    } else if (grid[i][j] == 0) {
                        // Espaço vazio (0) é pintado de branco
                        colorGrid[i][j] = Color.WHITE;
                    }
                }
            }

            // Preenche o GridPane com as cores atribuídas
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    Rectangle rect = new Rectangle(cellSize, cellSize);
                    rect.setStroke(Color.BLACK);
                    rect.setFill(colorGrid[i][j]);
                    gridPane.add(rect, j + 1, i + 1); // Adiciona a célula no grid
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        gridPane.setAlignment(Pos.CENTER); // Centraliza a malha com os números
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(gridPane);

        Scene scene = new Scene(stackPane, 800, 800); // Define o tamanho da janela
        primaryStage.setTitle("Simulador de Tráfego");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para propagar a estrada, mantendo a cor até o próximo cruzamento, sem mudar em curvas
    private void propagateRoad(int[][] grid, Color[][] colorGrid, int i, int j, Color roadColor, int currentDirection) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || colorGrid[i][j] != null) {
            return; // Se fora dos limites ou já pintado, saímos
        }

        if (isCrossroad(grid[i][j])) {
            colorGrid[i][j] = Color.LIGHTGRAY; // Pinta o cruzamento de cinza claro
            return; // Após o cruzamento, a propagação para
        }

        if (!isRoad(grid[i][j])) {
            return; // Se não for estrada, saímos
        }

        // Mantém a cor inicial da estrada até o cruzamento
        colorGrid[i][j] = roadColor;

        // Propaga nas direções possíveis, mantendo a cor até o próximo cruzamento
        // Movimentos verticais (cima e baixo)
        if (currentDirection == 1 || currentDirection == 3) {
            if (i + 1 < grid.length && (grid[i + 1][j] == currentDirection || isCrossroad(grid[i + 1][j]))) {
                propagateRoad(grid, colorGrid, i + 1, j, roadColor, currentDirection); // baixo
            }
            if (i - 1 >= 0 && (grid[i - 1][j] == currentDirection || isCrossroad(grid[i - 1][j]))) {
                propagateRoad(grid, colorGrid, i - 1, j, roadColor, currentDirection); // cima
            }
        }

        // Movimentos horizontais (direita e esquerda)
        if (currentDirection == 2 || currentDirection == 4) {
            if (j + 1 < grid[0].length && (grid[i][j + 1] == currentDirection || isCrossroad(grid[i][j + 1]))) {
                propagateRoad(grid, colorGrid, i, j + 1, roadColor, currentDirection); // direita
            }
            if (j - 1 >= 0 && (grid[i][j - 1] == currentDirection || isCrossroad(grid[i][j - 1]))) {
                propagateRoad(grid, colorGrid, i, j - 1, roadColor, currentDirection); // esquerda
            }
        }
    }

    // Método para determinar a cor com base na direção inicial da estrada
    private Color getColorForDirection(int direction) {
        switch (direction) {
            case 1: return Color.LIGHTBLUE;   // Estrada Cima
            case 2: return Color.LIGHTGREEN;  // Estrada Direita
            case 3: return Color.GRAY;        // Estrada Baixo
            case 4: return Color.LIGHTCORAL;  // Estrada Esquerda
            default: return Color.WHITE;      // Caso não seja estrada
        }
    }

    // Método para verificar se a célula é parte de uma estrada
    private boolean isRoad(int cell) {
        return cell >= 1 && cell <= 4; // Estradas são representadas pelos valores de 1 a 4
    }

    // Método para verificar se a célula é um cruzamento
    private boolean isCrossroad(int cell) {
        return cell >= 5 && cell <= 12; // Cruzamentos são representados pelos valores de 5 a 12
    }

    public static void main(String[] args) {
        launch(args);
    }
}