package com.simulator.view;

import com.simulator.model.Celula;
import com.simulator.model.Malha;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MalhaView {

    private GridPane gridPane;
    private double cellSize = 30;

    public MalhaView(Malha malha) {
        gridPane = new GridPane();
        inicializarMalha(malha);
    }

    private void inicializarMalha(Malha malha) {
        // Adiciona números nas colunas
        for (int col = 0; col < malha.getLenX(); col++) {
            Label colLabel = new Label(String.valueOf(col));
            GridPane.setHalignment(colLabel, HPos.CENTER);
            gridPane.add(colLabel, col + 1, 0);
        }

        // Adiciona números nas linhas
        for (int row = 0; row < malha.getLenY(); row++) {
            Label rowLabel = new Label(String.valueOf(row));
            gridPane.add(rowLabel, 0, row + 1);
        }

        // Preenche a malha com as cores corretas para cada célula
        for (int i = 0; i < malha.getLenY(); i++) {
            for (int j = 0; j < malha.getLenX(); j++) {
                Celula celula = malha.getCelula(i, j);
                Rectangle rect = new Rectangle(cellSize, cellSize);
                rect.setStroke(Color.BLACK);
                rect.setFill(getColorForCell(celula.getTipo()));
                gridPane.add(rect, j + 1, i + 1);
            }
        }

        gridPane.setAlignment(Pos.CENTER);
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    private Color getColorForCell(int cellValue) {
        switch (cellValue) {
            case 0:
                return Color.WHITE;        // Espaço vazio
            case 1:
                return Color.LIGHTBLUE;    // Estrada Cima
            case 2:
                return Color.LIGHTGREEN;   // Estrada Direita
            case 3:
                return Color.GRAY;         // Estrada Baixo
            case 4:
                return Color.LIGHTCORAL;   // Estrada Esquerda
            case 5:
                return Color.LIGHTGRAY;    // Cruzamento
            // Adicionar outros tipos se necessário
            default:
                return Color.LIGHTGRAY;   // Outros tipos desconhecidos
        }
    }
}
