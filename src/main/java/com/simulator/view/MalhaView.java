package com.simulator.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MalhaView {

    public void start(Stage primaryStage, GridPane container, HBox controlPanel) {
        container.setAlignment(Pos.CENTER); // Centraliza a malha com os números
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(container);

        Scene scene = new Scene(stackPane, 800, 800); // Define o tamanho da janela
        primaryStage.setTitle("Simulador de Tráfego");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



}