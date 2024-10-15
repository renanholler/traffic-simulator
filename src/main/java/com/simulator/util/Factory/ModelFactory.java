package com.simulator.util.Factory;

import com.simulator.model.Celula;
import com.simulator.model.Malha;

public class ModelFactory {

    public static Malha createMalha(int linhas, int colunas) {
        return new Malha(linhas, colunas);
    }

    public static Celula createCelula() {
        return new Celula();
    }

}
