package com.simulator.util.Factory;

import com.simulator.SimuladorTrafego;
import com.simulator.model.Malha;
import com.simulator.model.Veiculo;
import com.simulator.util.Strategy.SyncStrategy;

import java.util.List;
import java.util.Random;

public class VeiculoFactory {

    public static Veiculo createVeiculo(Malha malha, SyncStrategy strategy, SimuladorTrafego simuladorTrafego) {
        int velocidade = gerarVelocidadeAleatoria();
        int[] pontosEntrada = selecionarPontosEntrada(malha);
        return new Veiculo(velocidade, malha, strategy, pontosEntrada[0], pontosEntrada[1]);
    }

    private static int[] selecionarPontosEntrada(Malha malha) {
        List<int[]> pontosEntrada = malha.getPontosEntrada();
        Random random = new Random();
        return pontosEntrada.get(random.nextInt(pontosEntrada.size()));
    }

    /**
     * Gera uma velocidade aleatoria entre 500 e 1500 ms
     * @return int velocidade aleatoria
     */
    private static int gerarVelocidadeAleatoria() {
        return 500 + (new Random()).nextInt(1000);
    }

}
