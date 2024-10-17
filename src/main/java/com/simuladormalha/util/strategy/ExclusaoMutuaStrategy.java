package com.simuladormalha.util.strategy;

import com.simuladormalha.model.Celula;

import java.util.List;

public interface ExclusaoMutuaStrategy {
    boolean tentarReservar(List<Celula> caminho);
    void liberarCaminho(List<Celula> caminho);
    boolean isOcupado(Celula celula);
    boolean isCaminhoLivre(List<Celula> celula);
}
