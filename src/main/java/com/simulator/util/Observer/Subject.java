package com.simulator.util.Observer;

import com.simulator.controller.SimuladorController;

public interface Subject {
    void registraObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();

    void registraObserverRemove(ObserverRemove or);
    void notifyObserversRemove();
}
