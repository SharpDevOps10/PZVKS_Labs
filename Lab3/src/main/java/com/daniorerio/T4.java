package com.daniorerio;

public class T4 extends Thread {
    private final Monitor monitor;

    public T4(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку Т4
        System.out.println("T4 is started");
        int startIndex = 3 * Lab3.N / 4;

        // 1.	Введення Z, MD
        Data.readVector(Data.Z);
        Data.readMatrix(Data.MD);

        // 2.	Сигнал задачам T1, T2, T3 про введення Z, MD
        this.monitor.signalIn();

        // 3.	Чекати на введення даних в задачах T1, T3
        this.monitor.waitIn();

        // 4.	Обчислення 1: f4 = max(Zн)
        int f4 = Data.partiallyMaxInVector(Data.Z, startIndex);

        // 5.	Обчислення 2: f = max(f, f4)						КД1
        this.monitor.maxF(f4);               // КД1

        // 6.	Сигнал Т1, Т2, Т3 про завершення обчислення f
        this.monitor.signalMax();

        // 7.	Чекати на завершення обчислень f в потоках T2 T3 T4
        this.monitor.waitMax();

        // 8.	Копія f4 = f									КД2
        f4 = this.monitor.copyF();          // КД2

        // 9.	Копія e4 = e									КД3
        int e4 = this.monitor.copyE();          // КД3

        // 10.	Обчислення 3: MOн = e4*(MD*MCн) + f4*MXн
        int[][] MD_MC = Data.partiallyMultiplyMatrices(Data.MD, Data.MC, startIndex);
        int[][] e_MD_MC = Data.partiallyMultiplyMatrixScalar(MD_MC, e4, startIndex);
        int[][] f_MX = Data.partiallyMultiplyMatrixScalar(Data.MX, f4, startIndex);
        Data.partiallyAddMatrices(e_MD_MC, f_MX, startIndex, Data.MO);

        // 11.	Сигнал задачі T3 про обчислення MOн
        this.monitor.signalCalc();

        // Повідомлення про завершення виконання потоку T4
        System.out.println("T4 is finished");
    }
}