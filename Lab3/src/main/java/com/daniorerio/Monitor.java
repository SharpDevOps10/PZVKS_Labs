package com.daniorerio;

public class Monitor {
    private int e;
    private int f;
    private int fIn = 0;
    private int fMax = 0;
    private int fCalc = 0;

    // для сигналу про завершення уведення даних;
    public synchronized void signalIn() {
        this.fIn++;
        if (this.fIn == 3) this.notifyAll();
    }

    // для запису значення спільного ресурсу e;
    public synchronized void writeE(int value) {
        this.e = value;
    }

    // для очікування завершення уведення даних;
    public synchronized void waitIn() {
        try {
            if (this.fIn != 3) this.wait();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // для роботи зі спільним ресурсом f;
    public synchronized void maxF(int value) {
        this.f = Math.max(this.f, value);
    }

    // для сигналу про завершення обчислення f;
    public synchronized void signalMax() {
        this.fMax++;
        if (this.fMax == 4) this.notifyAll();
    }

    // для очікування завершення обчислення f;
    public synchronized void waitMax() {
        try {
            if (this.fMax != 4) this.wait();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // для копіювання спільного ресурсу f;
    public synchronized int copyF() {
        return this.f;
    }

    // для копіювання спільного ресурсу e;
    public synchronized int copyE() {
        return this.e;
    }

    // для сигналу про завершення обчислення MOн;
    public synchronized void signalCalc() {
        this.fCalc++;
        if (this.fCalc == 3) this.notifyAll();
    }

    // для очікування завершення обчислення MOн;
    public synchronized void waitCalc() {
        try {
            if (this.fCalc != 3) this.wait();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}