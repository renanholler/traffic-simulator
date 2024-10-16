package com.simuladormalha.controller;

import com.simuladormalha.model.MalhaViaria;
import com.simuladormalha.model.Veiculo;
import com.simuladormalha.util.factory.VeiculoFactory;
import com.simuladormalha.util.strategy.ExclusaoMutuaStrategy;

import java.util.ArrayList;
import java.util.List;

public class ControllerSimulacao {

    private MalhaViaria malha;
    private VeiculoFactory veiculoFactory;
    private List<Veiculo> veiculos;
    private boolean insercaoAtiva;
    private boolean simulacaoAtiva;

    public ControllerSimulacao(MalhaViaria malha, ExclusaoMutuaStrategy exclusaoMutua) {
        this.malha = malha;
        this.veiculoFactory = new VeiculoFactory(malha, exclusaoMutua);
        this.veiculos = new ArrayList<>();
        this.insercaoAtiva = false;
        this.simulacaoAtiva = false;
    }

    public void definirExclusaoMutuaStrategy(ExclusaoMutuaStrategy exclusaoMutua) {
        this.veiculoFactory.setExclusaoMutua(exclusaoMutua);
    }

    public void iniciarSimulacao(int maxVeiculos, int intervaloInsercao) {
        // em milissegundos
        this.insercaoAtiva = true;
        this.simulacaoAtiva = true;

        new Thread(() -> {
            while (simulacaoAtiva) {
                if(!insercaoAtiva){
                    continue;
                }
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
        this.simulacaoAtiva = false;
        for (Veiculo veiculo : veiculos) {
            veiculo.desativar();
        }
        for(int i = 0; i < malha.getLinhas(); i++) {
            for (int j = 0; j < malha.getColunas(); j++) {
                malha.getCelula(i,j).setReservada(false);
            }
        }
        veiculos.clear();
    }
}
