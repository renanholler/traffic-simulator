package com.simuladormalha.util.factory;

import com.simuladormalha.model.MalhaViaria;
import com.simuladormalha.model.Veiculo;
import com.simuladormalha.util.strategy.ExclusaoMutuaStrategy;

import java.util.List;
import java.util.Random;

public class VeiculoFactory {
    private MalhaViaria malha;
    private ExclusaoMutuaStrategy exclusaoMutua;
    private Random random;

    public VeiculoFactory(MalhaViaria malha, ExclusaoMutuaStrategy exclusaoMutua) {
        this.malha = malha;
        this.exclusaoMutua = exclusaoMutua;
        this.random = new Random();
    }

    public Veiculo criarVeiculo() {
        List<int[]> entradas = malha.getPontosEntrada();
        int[] entrada = entradas.get(random.nextInt(entradas.size()));
        int velocidade = 5 + random.nextInt(100);

        return new Veiculo(malha, entrada[0], entrada[1], velocidade, exclusaoMutua);
    }
}
