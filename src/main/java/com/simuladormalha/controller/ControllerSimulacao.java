package com.simuladormalha.controller;

import com.simuladormalha.model.MalhaViaria;
import com.simuladormalha.model.Veiculo;
import com.simuladormalha.util.factory.VeiculoFactory;
import com.simuladormalha.util.strategy.ExclusaoMutuaStrategy;

import java.util.ArrayList;
import java.util.List;

public class ControllerSimulacao {

    private MalhaViaria malha;
    private ExclusaoMutuaStrategy exclusaoMutua;
    private VeiculoFactory veiculoFactory;
    private List<Veiculo> veiculos;
    private int maxVeiculos;
    private int intervaloInsercao; // em milissegundos
    private boolean insercaoAtiva;

    public ControllerSimulacao(MalhaViaria malha, ExclusaoMutuaStrategy exclusaoMutua) {
        this.malha = malha;
        this.exclusaoMutua = exclusaoMutua;
        this.veiculoFactory = new VeiculoFactory(malha, exclusaoMutua);
        this.veiculos = new ArrayList<>();
        this.insercaoAtiva = false;
    }

    public void iniciarSimulacao(int maxVeiculos, int intervaloInsercao) {
        this.maxVeiculos = maxVeiculos;
        this.intervaloInsercao = intervaloInsercao;
        this.insercaoAtiva = true;

        new Thread(() -> {
            while (insercaoAtiva) {
                if (veiculos.size() < maxVeiculos) {
                    Veiculo veiculo = veiculoFactory.criarVeiculo();
                    if(malha.getMalha()[veiculo.getLinhaAtual()][veiculo.getColunaAtual()].estaOcupada()) {
                        continue;
                    } else {
                        malha.getMalha()[veiculo.getLinhaAtual()][veiculo.getColunaAtual()].setOcupada(true);
                    }
                    veiculos.add(veiculo);
                    veiculo.start();
                }
                try {
                    Thread.sleep(intervaloInsercao);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                veiculos.removeIf(v -> !v.isAlive());
            }
        }).start();
    }

    public void encerrarInsercao() {
        this.insercaoAtiva = false;
    }

    public void encerrarSimulacao() {
        this.insercaoAtiva = false;
        for (Veiculo veiculo : veiculos) {
            veiculo.desativar();
        }
        veiculos.clear();
    }
}
