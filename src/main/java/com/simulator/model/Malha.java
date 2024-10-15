package com.simulator.model;

import com.simulator.util.Factory.ModelFactory;

public class Malha {

    private Celula[][] grid;
    private int lenX;
    private int lenY;

    public Malha(int linhas, int colunas) {
        this.lenX = linhas;
        this.lenY = colunas;
        this.grid = new Celula[linhas][colunas];
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
        Celula celula = this.getCelula(posX, posY);
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
}
