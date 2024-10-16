package com.simuladormalha.view;

import com.simuladormalha.model.Celula;
import com.simuladormalha.model.MalhaViaria;

import javax.swing.*;
import java.awt.*;

public class PainelMalha extends JPanel {
    private MalhaViaria malha;

    public PainelMalha(MalhaViaria malha) {
        this.malha = malha;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int larguraCelula = 30;
        int alturaCelula = 30;

        for (int i = 0; i < malha.getLinhas(); i++) {
            for (int j = 0; j < malha.getColunas(); j++) {
                Celula celula = malha.getMalha()[i][j];
                if (celula.getTipo() != 0) {
                    g.setColor(getColorForCell(celula));
                    g.fillRect(j * larguraCelula, i * alturaCelula, larguraCelula, alturaCelula);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * larguraCelula, i * alturaCelula, larguraCelula, alturaCelula);
                }
            }
        }
    }

    private Color getColorForCell(Celula celula) {
        if(celula.estaOcupada()) {
            return Color.BLACK;
        }
        if(celula.estaReservada()) {
            return Color.RED;
        }
        switch (celula.getTipo()) {
            case 0:
                return Color.WHITE;        // Espaço vazio
            case 1:
                return Color.cyan;    // Estrada Cima
            case 2:
                return Color.GREEN;   // Estrada Direita
            case 3:
                return Color.GRAY;         // Estrada Baixo
            case 4:
                return Color.PINK;   // Estrada Esquerda
            case 5:
                return Color.lightGray;    // Cruzamento
            default:
                return Color.lightGray;   // Outros tipos desconhecidos
        }
    }
}

