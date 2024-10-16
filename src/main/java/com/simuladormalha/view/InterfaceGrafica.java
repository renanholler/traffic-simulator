package com.simuladormalha.view;

import com.simuladormalha.model.MalhaViaria;

import javax.swing.*;
import java.awt.*;

public class InterfaceGrafica extends JFrame {
    private MalhaViaria malha;
    private PainelMalha painelMalha;

    public InterfaceGrafica(MalhaViaria malha) {
        this.malha = malha;
        painelMalha = new PainelMalha(malha);

        setTitle("Simulação de Veículos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(painelMalha);

        setVisible(true);

        // Atualiza a interface a cada 100ms
        new Timer(100, e -> painelMalha.repaint()).start();
    }
}

