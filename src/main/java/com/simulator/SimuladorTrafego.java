package com.simulator;

import com.simulator.controller.SimuladorController;
import com.simulator.model.Malha;
import com.simulator.model.Veiculo;
import com.simulator.util.GridLoader;
import com.simulator.util.Observer.Observer;
import com.simulator.util.Observer.ObserverRemove;
import com.simulator.util.Strategy.MonitorStrategy;
import com.simulator.util.Strategy.SemaphoreStrategy;
import com.simulator.util.Strategy.SyncStrategy;
import com.simulator.view.MalhaView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.*;

import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimuladorTrafego extends Application implements Observer, ObserverRemove {

    private Malha malha;
    private Map<Veiculo, Rectangle> veiculoShapes;
    private SimuladorController controller;
    private SyncStrategy syncStrategy;
    private ComboBox<String> strategyComboBox;
    private int quantidadeVeiculos;
    private long intervaloInsercao;
    private MalhaView malhaView;

    @Override
    public void start(Stage stage) {
        veiculoShapes = new HashMap<>();

        BorderPane root = new BorderPane();

        try {
            malha = GridLoader.loadGridFromFile("/malhas/malha-exemplo-3.txt");
            malhaView = new MalhaView(malha);
        } catch (IOException e) {
            e.printStackTrace();
        }

        root.setCenter(malhaView.getGridPane());

        // Adicionar controles
        root.setTop(createControlPanel());

        Scene scene = new Scene(root, 1200, 850);
        stage.setTitle("Simulador de Tráfego");
        stage.setScene(scene);
        stage.show();
    }

    private HBox createControlPanel() {
        HBox controlPanel = new HBox(10);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setPadding(new Insets(10));

        // Campo para quantidade de veículos
        TextField quantidadeVeiculosField = new TextField();
        quantidadeVeiculosField.setPromptText("Quantidade de Veículos");

        // Campo para intervalo de inserção
        TextField intervaloInsercaoField = new TextField();
        intervaloInsercaoField.setPromptText("Intervalo de Inserção (ms)");

        // ComboBox para escolher a estratégia
        strategyComboBox = new ComboBox<>();
        strategyComboBox.getItems().addAll("Semáforos", "Monitores");
        strategyComboBox.setValue("Semáforos");

        // Botões
        Button iniciarButton = new Button("Iniciar Simulação");
        Button encerrarInsercaoButton = new Button("Encerrar Inserção");
        Button encerrarSimulacaoButton = new Button("Encerrar Simulação");

        iniciarButton.setOnAction(e -> {
            quantidadeVeiculos = Integer.parseInt(quantidadeVeiculosField.getText());
            intervaloInsercao = Long.parseLong(intervaloInsercaoField.getText());
            String strategy = strategyComboBox.getValue();

            if (strategy.equals("Semáforos")) {
                syncStrategy = new SemaphoreStrategy();
            } else {
                syncStrategy = new MonitorStrategy();
            }

            controller = new SimuladorController(malha, syncStrategy, this);

            controller.iniciarSimulacao(quantidadeVeiculos, intervaloInsercao);
        });

        encerrarInsercaoButton.setOnAction(e -> {
            if (controller != null) {
                controller.encerrarInsercao();
            }
        });

        encerrarSimulacaoButton.setOnAction(e -> {
            if (controller != null) {
                controller.encerrarSimulacao();
            }
        });

        controlPanel.getChildren().addAll(
                new Label("Qtd Veículos:"), quantidadeVeiculosField,
                new Label("Intervalo Inserção:"), intervaloInsercaoField,
                new Label("Estratégia:"), strategyComboBox,
                iniciarButton, encerrarInsercaoButton, encerrarSimulacaoButton
        );

        return controlPanel;
    }

    @Override
    public void update(Veiculo veiculo) {
        Platform.runLater(() -> {
            // Atualizar a posição do veículo na interface gráfica
            Rectangle shape = veiculoShapes.get(veiculo);
            if (shape == null) {
                shape = new Rectangle(30, 30, Color.BLACK);
                veiculoShapes.put(veiculo, shape);
                malhaView.getGridPane().add(shape, veiculo.getPosicaoX() + 1, veiculo.getPosicaoY() + 1);
            } else {
                GridPane.setColumnIndex(shape, veiculo.getPosicaoX() + 1);
                GridPane.setRowIndex(shape, veiculo.getPosicaoY() + 1);
            }
        });
    }

    @Override
    public void remove(Veiculo veiculo) {
        System.out.println("caiu aqui");
        Platform.runLater(() -> {
            Rectangle shape = veiculoShapes.remove(veiculo);
            if (shape != null) {
                malhaView.getGridPane().getChildren().remove(shape);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
