package com.simuladormalha.util.factory;

import com.simuladormalha.model.Celula;
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
        List<Celula> entradas = malha.getPontosEntrada();
        Celula entrada = entradas.get(random.nextInt(entradas.size()));
        int velocidade = 50 + random.nextInt(100);

        return new Veiculo(malha, entrada.getLinha(), entrada.getColuna(), velocidade, exclusaoMutua);
    }
}
