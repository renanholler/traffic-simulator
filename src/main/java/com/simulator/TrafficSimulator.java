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
            int[][] grid = loader.loadGridFromFile("/malhas/malha-exemplo-2.txt");
            double cellSize = 30;

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

            // Preenche a malha com as cores corretas para cada célula
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    Rectangle rect = new Rectangle(cellSize, cellSize);
                    rect.setStroke(Color.BLACK);

                    // Define a cor de acordo com o valor da célula
                    rect.setFill(getColorForCell(grid[i][j]));

                    // Adiciona a célula colorida ao grid
                    gridPane.add(rect, j + 1, i + 1);
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

    // Método para determinar a cor de uma célula com base no valor
    private Color getColorForCell(int cellValue) {
        switch (cellValue) {
            case 0: return Color.WHITE;        // Espaço vazio
            case 1: return Color.LIGHTBLUE;    // Estrada Cima
            case 2: return Color.LIGHTGREEN;   // Estrada Direita
            case 3: return Color.GRAY;         // Estrada Baixo
            case 4: return Color.LIGHTCORAL;   // Estrada Esquerda
            case 5: return Color.LIGHTGRAY;    // Cruzamento
            // Se houver outros tipos de cruzamento, você pode adicionar mais casos aqui
            default: return Color.LIGHTGRAY;   // Outros tipos desconhecidos
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}