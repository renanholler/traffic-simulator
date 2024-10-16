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

        int larguraCelula = getWidth() / malha.getColunas();
        int alturaCelula = getHeight() / malha.getLinhas();

        for (int i = 0; i < malha.getLinhas(); i++) {
            for (int j = 0; j < malha.getColunas(); j++) {
                Celula celula = malha.getMalha()[i][j];
                if (celula.getTipo() != 0) {
                    if (celula.estaOcupada()) {
                        g.setColor(Color.BLUE);
                    } else if (celula.estaReservada()) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.GRAY);
                    }
                    g.fillRect(j * larguraCelula, i * alturaCelula, larguraCelula, alturaCelula);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * larguraCelula, i * alturaCelula, larguraCelula, alturaCelula);
                }
            }
        }
    }
}

