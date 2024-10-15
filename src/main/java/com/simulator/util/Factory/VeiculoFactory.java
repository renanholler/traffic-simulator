package com.simulator.util.Factory;

import com.simulator.model.Malha;
import com.simulator.model.Veiculo;
import com.simulator.util.Strategy.SyncStrategy;

import java.util.Random;

public class VeiculoFactory {

    public static Veiculo createVeiculo(Malha malha, SyncStrategy strategy) {
        int velocidade = gerarVelocidadeAleatoria();
        int[] localizacao = getRandomStartPoint(malha);
        int startX = localizacao[0];
        int startY = localizacao[1];
        return new Veiculo(velocidade, malha, strategy, startX, startY);
    }

    private static int[] getRandomStartPoint(Malha malha) {
        //@TODO finalizar saporra
        return new int[]{1,0};
    }

    /**
     * Gera uma velocidade aleatoria entre 500 e 1500 ms
     * @return int velocidade aleatoria
     */
    private static int gerarVelocidadeAleatoria() {
        return 500 + (new Random()).nextInt(1000);
    }

}
