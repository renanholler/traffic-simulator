package com.simulator.util.Factory;

import com.simulator.model.Celula;
import com.simulator.model.Direction;
import com.simulator.model.Malha;
import com.simulator.model.Veiculo;
import com.simulator.util.Strategy.SyncStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class VeiculoFactory {

    public static Veiculo createVeiculo(Malha malha, SyncStrategy strategy) {
        int velocidade = gerarVelocidadeAleatoria();
        int[] pontosEntrada = selecionarPontosEntrada(malha);
        Direction direcaoInicial = determinarDirecaoInicial(malha, pontosEntrada);
        return new Veiculo(velocidade, malha, strategy, pontosEntrada[0], pontosEntrada[1]);
    }

    private static int[] selecionarPontosEntrada(Malha malha) {
        List<int[]> pontosEntrada = malha.getPontosEntrada();
        Random random = new Random();
        return pontosEntrada.get(random.nextInt(pontosEntrada.size()));
    }

    private static int gerarVelocidadeAleatoria() {
        return 10 + (new Random()).nextInt(150);
    }

    //**
    private static Direction determinarDirecaoInicial(Malha malha, int[] pontoEntrada) {
        Celula celulaEntrada = malha.getCelula(pontoEntrada[0], pontoEntrada[1]);
        Set<Direction> direcoesPermitidas = celulaEntrada.getDirecoesPermitidas();
        if (direcoesPermitidas.isEmpty()) {
            // Não há direções permitidas; retorna nulo ou uma direção padrão
            return null;
        } else {
            // Escolher aleatoriamente uma das direções permitidas
            List<Direction> direcoes = new ArrayList<>(direcoesPermitidas);
            Random rand = new Random();
            return direcoes.get(rand.nextInt(direcoes.size()));
        }
    }
}
