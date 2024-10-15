package com.simulator.util.Observer;

public interface Subject {
    void registraObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}
