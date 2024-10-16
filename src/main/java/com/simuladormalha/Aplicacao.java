package com.simuladormalha;

import com.simuladormalha.controller.ControllerSimulacao;
import com.simuladormalha.model.MalhaViaria;
import com.simuladormalha.util.strategy.ExclusaoMutuaStrategy;
import com.simuladormalha.util.strategy.SemaforoStrategy;
import com.simuladormalha.view.InterfaceGrafica;

public class Aplicacao {

    public static void main(String[] args) {
        try {
            // Carregar a malha viária
            MalhaViaria malha = new MalhaViaria("malha-exemplo-3.txt");

            // Escolher a estratégia de exclusão mútua
            ExclusaoMutuaStrategy exclusaoMutua = new SemaforoStrategy(malha);
            // ExclusaoMutuaStrategy exclusaoMutua = new MonitorStrategy();

            // Iniciar a interface gráfica
            InterfaceGrafica interfaceGrafica = new InterfaceGrafica(malha);

            // Controlador da simulação
            ControllerSimulacao controlador = new ControllerSimulacao(malha, exclusaoMutua);

            // Iniciar a simulação com 10 veículos e intervalo de inserção de 1 segundo
            controlador.iniciarSimulacao(10, 1000);

            // Encerrar a inserção após 30 segundos
            Thread.sleep(30000);
            controlador.encerrarInsercao();

            // Aguardar mais 10 segundos e encerrar a simulação
            Thread.sleep(10000);
            controlador.encerrarSimulacao();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
