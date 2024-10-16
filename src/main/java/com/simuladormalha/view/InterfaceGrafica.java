package com.simuladormalha.view;

import com.simuladormalha.model.MalhaViaria;

import javax.swing.*;
import java.awt.*;

public class InterfaceGrafica extends JFrame {
    private PainelMalha painelMalha;

    public InterfaceGrafica(MalhaViaria malha) {
        painelMalha = new PainelMalha(malha);

        setTitle("Simulação de Veículos");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Definindo layout para centralizar o painel
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Adicionando o painel ao frame com as configurações do GridBagLayout
        add(painelMalha, gbc);

        // Centraliza a própria janela na tela
        setLocationRelativeTo(null);

        setVisible(true);

        // Atualiza a interface a cada 100ms
        new Timer(100, e -> painelMalha.repaint()).start();
    }
}
