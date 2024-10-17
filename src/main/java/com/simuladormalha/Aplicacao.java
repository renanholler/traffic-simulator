package com.simuladormalha;

import com.simuladormalha.controller.ControllerSimulacao;
import com.simuladormalha.model.MalhaViaria;
import com.simuladormalha.view.InterfaceGrafica;

public class Aplicacao {

    public static void main(String[] args) {
        try {
            MalhaViaria malha = new MalhaViaria("/malhas/malha-exemplo-1.txt");

            ControllerSimulacao controlador = new ControllerSimulacao(malha, null);

            new InterfaceGrafica(malha, controlador);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
