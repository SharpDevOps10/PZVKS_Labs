package com.daniorerio;

public class T1 extends Thread {
    private final Monitor monitor;

    public T1(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку Т1
        System.out.println("T1 is started");
        int startIndex = 0;

        // 1.	Введення e, MX
        int e = Data.readScalar();
        this.monitor.writeE(e);
        Data.readMatrix(Data.MX);

        // 2. 	Сигнал задачам T2, T3, T4 про введення e, MX
        this.monitor.signalIn();

        // 3.	Чекати на введення даних в задачах T3, T4
        this.monitor.waitIn();

        // 4.	Обчислення 1: f1 = max(Zн)
        int f1 = Data.partiallyMaxInVector(Data.Z, startIndex);

        // 5.	Обчислення 2: f = max(f, f1)						КД1
        this.monitor.maxF(f1);               // КД1

        // 6.	Сигнал задачам T2, T3, T4 про завершення обчислення f
        this.monitor.signalMax();

        // 7.	Чекати на завершення обчислення f в задачах T2, T3, T4
        this.monitor.waitMax();

        // 8.	Копія f1 = f									КД2
        f1 = this.monitor.copyF();          // КД2

        // 9.	Копія e1 = e									КД3
        int e1 = this.monitor.copyE();          // КД3

        // 10.	Обчислення 3: MOн = e1*(MD*MCн) + f1*MXн
        int[][] MD_MC = Data.partiallyMultiplyMatrices(Data.MD, Data.MC, startIndex);
        int[][] e_MD_MC = Data.partiallyMultiplyMatrixScalar(MD_MC, e1, startIndex);
        int[][] f_MX = Data.partiallyMultiplyMatrixScalar(Data.MX, f1, startIndex);
        Data.partiallyAddMatrices(e_MD_MC, f_MX, startIndex, Data.MO);

        // 11.	Сигнал задачі T3 про обчислення MOн
        this.monitor.signalCalc();

        // Повідомлення про завершення виконання потоку T1
        System.out.println("T1 is finished");
    }
}