package com.daniorerio;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;

public class T4 extends Thread {
    private final Data data;
    int d4;
    int p4;
    int a4;

    public T4(Data data) {
        this.data = data;
    }

    @Override
    public void run() {
        try {
            // Повідомлення про початок виконання потоку Т4
            System.out.println("T4 started");

            // Вимірювання часу виконання програми
            double startTime = System.currentTimeMillis();

            // 1. Введення Z, MT, d
            // заповнення Z
            // заповнення Z різними значеннями для демонстрації сортування
            Arrays.fill(data.Z, 1);
            data.Z[0] = 7;
            data.Z[1] = 6;
            data.Z[2] = 5;

            // заповнення MT
            for (int i = 0; i < data.N; i++) {
                for (int j = 0; j < data.N; j++) {
                    data.MT[i][j] = 1;
                }
            }

            // заповнення d
            data.d = 1;

            // 2. Сигнал Т1, Т2, Т3 про введення Z, MT, d\
            // Чекати на введення даних в задачах Т1 і Т3
            data.B0.await();

            // 4.	Копія d4 = d (КД1)
            data.S0.acquire();
            d4 = data.d;
            data.S0.release();

            // 5. Обчислення Vн = sort(d4 * Bн + Z * MMн)
            // обчислення: Vh_part1 = d4 * Bн
            int[] Vh_part1 = Data.multiplyScalarByVector(d4, Data.takePartOfVector(data.B, data.H * 3, data.H * 4));

            // обчислення: Vh_part2 = Z * MMн
            int[] Vh_part2 = Data.multiplyMatrixByVector(
                    Data.takePartOfMatrixRows(data.MM, data.H * 3, data.H * 4),
                    data.Z
            );

            // обчислення 1: Vh = sort(Vh_part1 + Vh_part2)
            int[] Vh = Data.sumOfVectors(Vh_part1, Vh_part2);
            Data.insertSubvectorIntoVector(Vh, data.V, data.H * 3);
            Data.mergeSort(data.V, data.H * 3, data.H * 4 - 1);

            //  6. Сигнал задачі Т3 про завершення обчислення Vн
            data.S4.release();

            //  7. Чекати на завершення обчислення V у Т1
            data.S1.acquire();

            //  8. Обчислення a4 = Bн * Zн
            a4 = Data.scalarProductOfVectors(
                    Data.getSubvectorFromVector(data.B, data.H * 3, data.H),
                    Data.getSubvectorFromVector(data.Z, data.H * 3, data.H)
            );

            // 9. КД2: a = a + a4
            data.a.updateAndGet(a -> a + a4);

            // 10. Сигнал Т1, Т2, Т3 про завершення обчислення a
            data.S8.release(3);

            // 11. Чекати на завершення обчислення a у Т1, Т2, Т3
            data.S5.acquire(); // Т1
            data.S6.acquire(); // Т2
            data.S7.acquire(); // Т3

            //  12. КД3: копіювання p4 = p
            synchronized (data.CS2) {
                p4 = data.p;
            }

            //  13. КД4: копіювання a4 = a
            synchronized (data.CS1) {
                a4 = data.a.get();
            }

            // 14. Обчислення Ан = p4 * V * (MXн * MT) + a4 * Zн
            // обчислення: MXhMT = MXн * MT
            int[][] MXh = Data.takePartOfMatrixRows(data.MX, data.H * 3, data.H * 4);
            int[][] MXhMT = Data.multiplyMatrixByMatrix(MXh, data.MT);

            // обчислення: MXhMT = MXн * MT
            int[] VMXhMT = Data.multiplyMatrixByVector(MXhMT, data.V);
            // обчислення: a4Zh = a4 * Zн
            int[] a4Zh = Data.multiplyScalarByVector(a4, Data.takePartOfVector(data.Z, data.H * 3, data.H * 4));

            // обчислення : Ан = p * VMXhMT + a4Zh
            int[] An = Data.sumOfVectors(
                    Data.multiplyScalarByVector(p4, VMXhMT),
                    a4Zh
            );

            Data.insertSubvectorIntoVector(An, data.A, data.H * 3);

            // 15. Чекати на завершення обчислення A в Т1, Т2, Т3
            data.S9.acquire(3);

            //  16. Виведення результату A
            System.out.println("Final A = " + Arrays.toString(data.A));

            // Повідомлення про завершення виконання потоку T4
            System.out.println("T4 finished");

            // Вивід часу виконання програми
            double endTime = System.currentTimeMillis();
            double executionTimeSec = (endTime - startTime) / 1000.0;
            System.out.println("Execution time: " + String.format("%.3f", executionTimeSec) + " s");
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}