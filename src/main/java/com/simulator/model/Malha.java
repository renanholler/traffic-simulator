package com.simulator.model;

import com.simulator.util.Factory.ModelFactory;

import java.util.ArrayList;
import java.util.List;

public class Malha {

    private Celula[][] grid;
    private int lenX;
    private int lenY;

    public Malha(int linhas, int colunas) {
        this.lenX = colunas;
        this.lenY = linhas;
        this.grid = new Celula[colunas][linhas];
        this.initGrid();
    }

    private void initGrid() {
        for(int i = 0; i < this.lenX; i++) {
            for(int j = 0; j < this.lenY; j++) {
                this.grid[i][j] = ModelFactory.createCelula();
            }
        }
    }

    public Celula[][] getGrid() {
        return grid;
    }

    public void setGrid(Celula[][] grid) {
        this.grid = grid;
    }

    public Celula getCelula(int linha, int coluna) {
        return grid[linha][coluna];
    }

    public void setTipoCelula(int posX, int posY, int tipo) {
        Celula celula = this.getCelula(posY, posX);
        celula.setTipo(tipo);
    }

    public int getLenX() {
        return lenX;
    }

    public void setLenX(int lenX) {
        this.lenX = lenX;
    }

    public int getLenY() {
        return lenY;
    }

    public void setLenY(int lenY) {
        this.lenY = lenY;
    }

    public List<int[]> getPontosEntrada() {
        List<int[]> pontosEntrada = new ArrayList<>();
        for(int i = 0; i < lenX; i++) {
            for(int j = 0; j < lenY; j++) {
                if(ehPontoEntrada(getLado(i,j), getCelula(i,j).getTipo())) {
                    pontosEntrada.add(new int[]{i,j});
                }
            }
        }
        return pontosEntrada;
    }

    public int getLado(int x, int y) {
        if(x == 0) {
            return 1;
        }
        if(x == lenX - 1) {
            return 3;
        }
        if(y == 0) {
            return 2;
        }
        if(y == lenY - 1) {
            return 4;
        }
        return 0;
    }


    private boolean ehPontoEntrada(int lado, int tipoCelula) {
        if (lado == 1 && tipoCelula == 2) {
            return true;
        }
        if (lado == 2 && tipoCelula == 3) {
            return true;
        }
        if (lado == 3 && tipoCelula == 4) {
            return true;
        }
        return lado == 4 && tipoCelula == 1;
    }

}
