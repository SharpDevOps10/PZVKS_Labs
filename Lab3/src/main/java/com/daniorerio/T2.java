package com.daniorerio;

public class T2 extends Thread {
    private final Monitor monitor;

    public T2(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку Т2
        System.out.println("T2 is started");
        int startIndex = Lab3.N / 4;

        // 1.	Чекати на введення даних в задачі T1, T3, T4
        this.monitor.waitIn();

        // 2.	Обчислення 1: f2 = max(Zн)
        int f2 = Data.partiallyMaxInVector(Data.Z, startIndex);

        // 3.	Обчислення 2: f = max(f, f2)						КД1
        this.monitor.maxF(f2);               // КД1

        // 4.	Сигнал задачам T1, T3, T4 про завершення обчислення f
        this.monitor.signalMax();

        // 5.	Чекати на завершення обчислення f в задачах T1, T3, T4
        this.monitor.waitMax();

        // 6. 	Копія f2 = f									КД2
        f2 = this.monitor.copyF();          // КД2

        // 7.	Копія e2 = e									КД3
        int e2 = this.monitor.copyE();          // КД3

        // 8.	Обчислення 3: MOн = e2*(MD*MCн) + f2*MXн
        int[][] MD_MC = Data.partiallyMultiplyMatrices(Data.MD, Data.MC, startIndex);
        int[][] e_MD_MC = Data.partiallyMultiplyMatrixScalar(MD_MC, e2, startIndex);
        int[][] f_MX = Data.partiallyMultiplyMatrixScalar(Data.MX, f2, startIndex);
        Data.partiallyAddMatrices(e_MD_MC, f_MX, startIndex, Data.MO);

        // 9.	Сигнал задачі T3 про обчислення MOн
        this.monitor.signalCalc();

        // Повідомлення про завершення виконання потоку T2
        System.out.println("T2 is finished");
    }
}