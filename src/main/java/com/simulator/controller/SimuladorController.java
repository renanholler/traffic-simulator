package com.simulator.controller;

import com.simulator.SimuladorTrafego;
import com.simulator.model.Malha;
import com.simulator.model.Veiculo;
import com.simulator.util.Factory.VeiculoFactory;
import com.simulator.util.GridLoader;
import com.simulator.util.Observer.ObserverRemove;
import com.simulator.util.Strategy.SyncStrategy;
import com.simulator.view.MalhaView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SimuladorController implements ObserverRemove {
    private final Malha malha;
    private final List<Veiculo> veiculosAtivos;
    private final SyncStrategy syncStrategy;
    private boolean insercaoAtiva = true;
    private SimuladorTrafego simuladorTrafego;

    public SimuladorController(Malha malha, SyncStrategy syncStrategy, SimuladorTrafego simuladorTrafego) {
        this.malha = malha;
        this.veiculosAtivos = new ArrayList<>();
        this.syncStrategy = syncStrategy;
        this.simuladorTrafego = simuladorTrafego;
    }

    public void iniciarSimulacao(int quantidadeVeiculos, long intervaloInsercao) {
        new Thread(() -> {
            while(insercaoAtiva) {
                if(veiculosAtivos.size() < quantidadeVeiculos) {
                    Veiculo veiculo = VeiculoFactory.createVeiculo(malha, syncStrategy);
                    veiculo.registraObserver(simuladorTrafego);
                    veiculo.registraObserverRemove(this);
                    veiculo.registraObserverRemove(simuladorTrafego);
                    veiculosAtivos.add(veiculo);
                    new Thread(veiculo).start();
                }
                try {
                    Thread.sleep(intervaloInsercao);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public void encerrarInsercao() {
        insercaoAtiva = false;
    }

    public void encerrarSimulacao() {
        insercaoAtiva = false;
        while (!veiculosAtivos.isEmpty()) {
            Veiculo veiculo = veiculosAtivos.removeFirst();
            veiculo.parar();
        }
    }


    @Override
    public void remove(Veiculo veiculo) {
        this.veiculosAtivos.remove(veiculo);
    }
}
