package com.simulator.model;

import com.simulator.util.Observer.Observer;
import com.simulator.util.Observer.Subject;
import com.simulator.util.Strategy.SyncStrategy;

import java.util.*;

public class Veiculo implements Runnable, Subject {

    private int velocidade;
    private int posicaoX;
    private int posicaoY;
    private Malha malha;
    private SyncStrategy syncStrategy;
    private boolean running = true;
    private List<Observer> observers;
    private Direction direcaoAtual;

    public Veiculo(int velocidade, Malha malha, SyncStrategy syncStrategy, int startX, int startY) {
        this.velocidade = velocidade;
        this.posicaoX = startX;
        this.posicaoY = startY;
        this.malha = malha;
        this.syncStrategy = syncStrategy;
        this.observers = new ArrayList<>();
        this.direcaoAtual = determinarDirecaoInicial();
    }

    @Override
    public void run() {
        while (running) {
            try {
                mover();
                Thread.sleep(velocidade);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private Direction determinarDirecaoInicial() {
        Celula celulaAtual = malha.getCelula(posicaoX, posicaoY);
        Set<Direction> direcoesPermitidas = celulaAtual.getDirecoesPermitidas();
        if (direcoesPermitidas.isEmpty()) {
            // Não há direções permitidas; encerrar veículo
            running = false;
            return null;
        } else {
            // Escolher aleatoriamente uma das direções permitidas
            List<Direction> direcoes = new ArrayList<>(direcoesPermitidas);
            Random rand = new Random();
            return direcoes.get(rand.nextInt(direcoes.size()));
        }
    }

    private void mover() throws InterruptedException {
        // Verificar se chegou ao destino
        if (chegouAoDestino()) {
            running = false;
            liberarCelulaAtual();
            notifyObservers();
            return;
        }

        // Decidir a direção desejada (pode ser aleatória ou baseada em alguma lógica)
        Direction direcaoDesejada = decidirDirecao();

        // Calcular o trajeto completo
        List<int[]> trajeto = calcularTrajetoCompleto(direcaoDesejada);

        // Obter as células do trajeto
        List<Celula> celulasTrajeto = new ArrayList<>();
        for (int[] pos : trajeto) {
            int x = pos[0];
            int y = pos[1];
            // Verificar se a posição está dentro dos limites
            if (x < 0 || x >= malha.getLenX() || y < 0 || y >= malha.getLenY()) {
                // Trajeto inválido; encerrar o veículo
                running = false;
                liberarCelulaAtual();
                notifyObservers();
                return;
            }
            Celula celula = malha.getCelula(x, y);
            celulasTrajeto.add(celula);
        }

        // Validar se todas as células estão livres
        boolean podeMover = true;
        for (Celula celula : celulasTrajeto) {
            synchronized (celula) {
                if (celula.isOcupada()) {
                    podeMover = false;
                    break;
                }
            }
        }

        if (!podeMover) {
            // Não pode mover; aguardar
            Thread.sleep(velocidade);
            return;
        }

        // Adquirir locks em todas as células do trajeto
        syncStrategy.acquire(celulasTrajeto.toArray(new Celula[0]));

        // Liberar a célula atual
        liberarCelulaAtual();

        // Movimentar o veículo ao longo do trajeto
        for (int[] pos : trajeto) {
            posicaoX = pos[0];
            posicaoY = pos[1];
            notifyObservers();
            Thread.sleep(velocidade);
        }

        // Atualizar a direção atual
        direcaoAtual = direcaoDesejada;

        // Liberar as células do trajeto
        syncStrategy.release(celulasTrajeto.toArray(new Celula[0]));
    }

    private List<int[]> calcularTrajetoCompleto(Direction direcaoDesejada) {
        List<int[]> trajeto = new ArrayList<>();
        int x = posicaoX;
        int y = posicaoY;

        // Adicionar posição inicial
        trajeto.add(new int[]{x, y});

        // Obter a célula atual
        Celula celulaAtual = malha.getCelula(x, y);

        // Calcular próxima posição
        int nextX = x + deltaX(direcaoDesejada);
        int nextY = y + deltaY(direcaoDesejada);

        // Adicionar próxima posição ao trajeto
        trajeto.add(new int[]{nextX, nextY});

        Celula celulaProxima = malha.getCelula(nextX, nextY);

        // Verificar se a próxima célula é um cruzamento
        if (celulaProxima != null && celulaProxima.isCruzamento()) {
            // Implementar lógica específica para cruzamentos
            // Por exemplo, se o veículo quer virar, adicionar células adicionais ao trajeto
            if (!direcaoDesejada.equals(direcaoAtual)) {
                // O veículo está mudando de direção; adicionar células da curva
                // Exemplo: adicionar a célula intermediária da curva
                int curvaX = nextX + deltaX(direcaoDesejada);
                int curvaY = nextY + deltaY(direcaoDesejada);
                trajeto.add(new int[]{curvaX, curvaY});
            }
        }

        return trajeto;
    }



    private Direction decidirDirecao() {
        Celula celulaAtual = malha.getCelula(posicaoX, posicaoY);
        Set<Direction> direcoesPossiveis = new HashSet<>(celulaAtual.getDirecoesPermitidas());

        // Remover a direção oposta para evitar retorno
        direcoesPossiveis.remove(getDirecaoOposta(direcaoAtual));

        if (direcoesPossiveis.isEmpty()) {
            // Não há direções disponíveis; mantém a direção atual
            return direcaoAtual;
        }

        // Escolher aleatoriamente uma das direções possíveis
        List<Direction> direcoes = new ArrayList<>(direcoesPossiveis);
        Random rand = new Random();
        return direcoes.get(rand.nextInt(direcoes.size()));
    }

    private int deltaX(Direction dir) {
        switch (dir) {
            case LEFT:
                return -1;
            case RIGHT:
                return 1;
            default:
                return 0;
        }
    }

    private int deltaY(Direction dir) {
        switch (dir) {
            case UP:
                return -1;
            case DOWN:
                return 1;
            default:
                return 0;
        }
    }


    private int[] calcularProximaPosicao() {
        int nextX = posicaoX;
        int nextY = posicaoY;

        // Calcular a próxima posição com base na direção atual
        switch (direcaoAtual) {
            case UP:
                nextY -= 1;
                break;
            case DOWN:
                nextY += 1;
                break;
            case LEFT:
                nextX -= 1;
                break;
            case RIGHT:
                nextX += 1;
                break;
        }

        // Verificar limites da malha
        if (nextX < 0 || nextX >= malha.getLenX() || nextY < 0 || nextY >= malha.getLenY()) {
            return null;
        }

        Celula proximaCelula = malha.getCelula(nextX, nextY);

        if (proximaCelula == null || proximaCelula.getDirecoesPermitidas().isEmpty()) {
            return null;
        }

        // Se a próxima célula for um cruzamento, decidir nova direção antes de entrar
        if (proximaCelula.isCruzamento()) {
            decidirDirecaoNoCruzamento(proximaCelula);
            // Recalcular a próxima posição com a nova direção
            nextX = posicaoX;
            nextY = posicaoY;
            switch (direcaoAtual) {
                case UP:
                    nextY -= 1;
                    break;
                case DOWN:
                    nextY += 1;
                    break;
                case LEFT:
                    nextX -= 1;
                    break;
                case RIGHT:
                    nextX += 1;
                    break;
            }

            // Verificar limites novamente
            if (nextX < 0 || nextX >= malha.getLenX() || nextY < 0 || nextY >= malha.getLenY()) {
                return null;
            }

            proximaCelula = malha.getCelula(nextX, nextY);

            if (proximaCelula == null || !proximaCelula.getDirecoesPermitidas().contains(direcaoAtual)) {
                return null;
            }
        } else {
            // Verificar se a próxima célula permite a direção atual
            if (!proximaCelula.getDirecoesPermitidas().contains(direcaoAtual)) {
                return null;
            }
        }

        return new int[]{nextX, nextY};
    }

    private void decidirDirecaoNoCruzamento(Celula celulaCruzamento) {
        Set<Direction> direcoesPossiveis = new HashSet<>(celulaCruzamento.getDirecoesPermitidas());
        // Remover a direção oposta para evitar retorno
        direcoesPossiveis.remove(getDirecaoOposta(direcaoAtual));

        if (!direcoesPossiveis.isEmpty()) {
            List<Direction> direcoes = new ArrayList<>(direcoesPossiveis);
            Random rand = new Random();
            direcaoAtual = direcoes.get(rand.nextInt(direcoes.size()));
        }
        // Caso contrário, mantém a direção atual
    }

    private Direction getDirecaoOposta(Direction dir) {
        switch (dir) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            default:
                return null;
        }
    }

    private boolean chegouAoDestino() {
        return malha.isPontoSaida(posicaoX, posicaoY);
    }

    private void liberarCelulaAtual() {
        Celula celulaAtual = malha.getCelula(posicaoX, posicaoY);
        syncStrategy.release(new Celula[]{celulaAtual});
    }

    // Métodos do padrão Observer (registraObserver, removeObserver, notifyObservers)

    @Override
    public void registraObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    public int getPosicaoX() {
        return posicaoX;
    }

    public int getPosicaoY() {
        return posicaoY;
    }
}
