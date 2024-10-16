package com.simulator.controller;

import com.simulator.model.Malha;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MalhaController {

    private final Malha malha;
    private final int CELL_SIZE = 30;

    public MalhaController(Malha malha) {
        this.malha = malha;
    }

    public void inicializaGridPane(GridPane gridPane) {
        // Adiciona números nas colunas (0 a n-1)
        for (int col = 0; col < this.malha.getGrid()[0].length; col++) {
            Label colLabel = new Label(String.valueOf(col));
            GridPane.setHalignment(colLabel, HPos.CENTER); // Centraliza o label
            gridPane.add(colLabel, col + 1, 0); // Ajusta para ter espaço à esquerda
        }

        // Adiciona números nas linhas (0 a n-1)
        for (int row = 0; row < this.malha.getGrid().length; row++) {
            Label rowLabel = new Label(String.valueOf(row));
            gridPane.add(rowLabel, 0, row + 1); // Ajusta para ter espaço acima
        }

        // Preenche a malha com as cores corretas para cada célula
        for (int i = 0; i < this.malha.getGrid().length; i++) {
            for (int j = 0; j < this.malha.getGrid()[i].length; j++) {
                Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE);
                rect.setStroke(Color.BLACK);

                // Define a cor de acordo com o valor da célula
                rect.setFill(getColorForCell(this.malha.getGrid()[i][j].getTipo()));

                // Adiciona a célula colorida ao grid
                gridPane.add(rect, j + 1, i + 1);
            }
        }
    }

    // Método para determinar a cor de uma célula com base no valor
    private Color getColorForCell(int cellValue) {
        return switch (cellValue) {
            case 0 -> Color.WHITE;        // Espaço vazio
            case 1 -> Color.LIGHTBLUE;    // Estrada Cima
            case 2 -> Color.LIGHTGREEN;   // Estrada Direita
            case 3 -> Color.GRAY;         // Estrada Baixo
            case 4 -> Color.LIGHTCORAL;   // Estrada Esquerda
            case 5 -> Color.LIGHTGRAY;    // Cruzamento
            // Se houver outros tipos de cruzamento, você pode adicionar mais casos aqui
            default -> Color.LIGHTGRAY;   // Outros tipos desconhecidos
        };
    }
}
