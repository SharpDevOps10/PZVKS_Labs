package com.daniorerio;

public class T3 extends Thread {
    private final Monitor monitor;

    public T3(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку Т3
        System.out.println("T3 is started");
        int startIndex = Lab3.N / 2;

        // 1.	Введення MC
        Data.readMatrix(Data.MC);

        // 2.	Сигнал задачам T1, T2, T4 про введення MC
        this.monitor.signalIn();

        // 3.	Чекати на введення даних в задачах T1, T4
        this.monitor.waitIn();

        // 4.	Обчислення 1: f3 = max(Zн)
        int f3 = Data.partiallyMaxInVector(Data.Z, startIndex);

        // 5.	Обчислення 2: f = max(f, f3)						КД1
        this.monitor.maxF(f3);               // КД1

        // 6.	Сигнал Т1, Т2, Т4 про завершення обчислення f
        this.monitor.signalMax();

        // 7.	Чекати на завершення обчислень f в потоках T1 T2 T4
        this.monitor.waitMax();

        // 8.	Копія f3 = f									КД2
        f3 = this.monitor.copyF();          // КД2

        // 9.	Копія e3 = e									КД3
        int e3 = this.monitor.copyE();          // КД3

        // 10.	Обчислення 3: MOн = e3*(MD*MCн) + f3*MXн
        int[][] MD_MC = Data.partiallyMultiplyMatrices(Data.MD, Data.MC, startIndex);
        int[][] e_MD_MC = Data.partiallyMultiplyMatrixScalar(MD_MC, e3, startIndex);
        int[][] f_MX = Data.partiallyMultiplyMatrixScalar(Data.MX, f3, startIndex);
        Data.partiallyAddMatrices(e_MD_MC, f_MX, startIndex, Data.MO);

        // 11.	Чекати на завершення обчислення MOн в потоках T1 T2 T4
        this.monitor.waitCalc();

        // 12.	Виведення MO
        Data.printResultMatrix("T3", Data.MO);

        // Повідомлення про завершення виконання потоку T3
        System.out.println("T3 is finished");
    }
}