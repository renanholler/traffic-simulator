package com.simuladormalha.view;

import com.simuladormalha.controller.ControllerSimulacao;
import com.simuladormalha.model.MalhaViaria;
import com.simuladormalha.util.strategy.ExclusaoMutuaStrategy;
import com.simuladormalha.util.strategy.MonitorStrategy;
import com.simuladormalha.util.strategy.SemaforoStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceGrafica extends JFrame {
    private MalhaViaria malha;
    private PainelMalha painelMalha;
    private ControllerSimulacao controlador;
    private JTextField campoMaxVeiculos;
    private JTextField campoIntervaloInsercao;
    private JButton botaoIniciar;
    private JButton botaoEncerrarInsercao;
    private JButton botaoRetomarInsercao;
    private JButton botaoEncerrarSimulacao;
    private JComboBox<String> comboExclusaoMutua;

    public InterfaceGrafica(MalhaViaria malha, ControllerSimulacao controlador) {
        this.malha = malha;
        this.controlador = controlador;
        painelMalha = new PainelMalha(malha);

        setTitle("Simulação de Veículos");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel de controles
        JPanel painelControles = new JPanel();
        painelControles.setLayout(new GridLayout(7, 2, 5, 5));

        painelControles.add(new JLabel("Quantidade Máxima de Veículos:"));
        campoMaxVeiculos = new JTextField("10");
        painelControles.add(campoMaxVeiculos);

        painelControles.add(new JLabel("Intervalo de Inserção (ms):"));
        campoIntervaloInsercao = new JTextField("1000");
        painelControles.add(campoIntervaloInsercao);

        painelControles.add(new JLabel("Exclusão Mútua:"));
        comboExclusaoMutua = new JComboBox<>(new String[]{"Semáforo", "Monitor"});
        painelControles.add(comboExclusaoMutua);

        botaoIniciar = new JButton("Iniciar Simulação");
        painelControles.add(botaoIniciar);

        botaoEncerrarInsercao = new JButton("Parar Inserção");
        painelControles.add(botaoEncerrarInsercao);

        botaoRetomarInsercao = new JButton("Retomar Inserção");
        painelControles.add(botaoRetomarInsercao);

        botaoEncerrarSimulacao = new JButton("Encerrar Simulação");
        painelControles.add(botaoEncerrarSimulacao);

        add(painelControles, BorderLayout.EAST);
        add(painelMalha, BorderLayout.CENTER);

        // Eventos dos botões
        botaoIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSimulacao();
            }
        });

        botaoEncerrarInsercao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.encerrarInsercao();
            }
        });

        botaoRetomarInsercao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.retornarInsercao();
            }
        });

        botaoEncerrarSimulacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encerrarSimulacao();
            }
        });

        setVisible(true);

        // Atualiza a interface a cada 100ms
        new Timer(16, e -> painelMalha.repaint()).start();
    }

    private void iniciarSimulacao() {
        try {
            int maxVeiculos = Integer.parseInt(campoMaxVeiculos.getText());
            int intervaloInsercao = Integer.parseInt(campoIntervaloInsercao.getText());

            String exclusaoMutuaSelecionada = (String) comboExclusaoMutua.getSelectedItem();
            ExclusaoMutuaStrategy exclusaoMutua;

            if ("Semáforo".equals(exclusaoMutuaSelecionada)) {
                exclusaoMutua = new SemaforoStrategy(malha);
            } else {
                exclusaoMutua = new MonitorStrategy();
            }

            controlador.definirExclusaoMutuaStrategy(exclusaoMutua);
            controlador.iniciarSimulacao(maxVeiculos, intervaloInsercao);

            botaoIniciar.setEnabled(false);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valores inválidos nos campos de entrada.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void encerrarSimulacao() {
        botaoIniciar.setEnabled(true);
        controlador.encerrarSimulacao();
    }
}
