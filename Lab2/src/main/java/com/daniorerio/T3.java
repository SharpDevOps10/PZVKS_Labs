package com.daniorerio;

import java.util.concurrent.BrokenBarrierException;

public class T3 extends Thread {
    private final Data data;
    int d3;
    int p3;
    int a3;

    public T3(Data data) {
        this.data = data;
    }

    @Override
    public void run() {
        try {
            // Повідомлення про початок виконання потоку Т3
            System.out.println("T3 started");

            // 1. Введення p
            data.p = 1;

            // 2. Сигнал задачам Т1, Т2, Т4 про завершення введення p
            // 3. Чекати на введення даних в задачі Т1 і Т4
            data.B0.await();

            // 4. Копія d3 = d (КД1)
            data.S0.acquire();
            d3 = data.d;
            data.S0.release();

            // 5. Обчислення 1: Vн = sort(d3 * Bн + Z * MMн)
            // обчислення: Vh_part1 = d3 * Bн
            int[] Vh_part1 = Data.multiplyScalarByVector(d3, Data.takePartOfVector(data.B, data.H * 2, data.H * 3));
            // обчислення: Vh_part2 = Z * MMн
            int[] Vh_part2 = Data.multiplyMatrixByVector(
                    Data.takePartOfMatrixRows(data.MM, data.H * 2, data.H * 3),
                    data.Z
            );

            // обчислення 1: Vh = sort(Vh_part1 + Vh_part2)
            int[] Vh = Data.sumOfVectors(Vh_part1, Vh_part2);
            Data.insertSubvectorIntoVector(Vh, data.V, data.H * 2);
            Data.mergeSort(data.V, data.H * 2, data.H * 3 - 1);

            // 6. Чекати на завершення обчислення Vн у Т4
            data.S4.acquire();

            // 7. Обчислення 2: V2н = merge(Vн, Vн)
            Data.merge(data.V, data.H * 2, data.H * 3 - 1, data.H * 4 - 1);

            // 8. Сигнал задачі Т1 про завершення обчислення V2н
            data.S3.release();

            // 9. Чекати на завершення обчислення V у Т1
            data.S1.acquire();

            // 10. Обчислення 3: a3 = (Bн * Zн)
            a3 = Data.scalarProductOfVectors(
                    Data.getSubvectorFromVector(data.B, data.H * 2, data.H),
                    Data.getSubvectorFromVector(data.Z, data.H * 2, data.H)
            );

            // 11. КД2: a = a + a3
            data.a.updateAndGet(a -> a + a3);

            // 12. Сигнал задачам Т1, Т2, Т4 про завершення обчислення a
            data.S7.release(3);

            // 13. Чекати на завершення обчислення a у задачах Т1, Т2, Т4
            data.S5.acquire(); // Т1
            data.S6.acquire(); // Т2
            data.S8.acquire(); // Т4

            // 14. КД3: захищене копіювання p
            synchronized (data.CS2) {
                p3 = data.p;
            }

            // 15. КД4: захищене копіювання a
            synchronized (data.CS1) {
                a3 = data.a.get();
            }

            // 16. Обчислення 4: Ан = p3 * V * (MXн * MT) + a3 * Zн

            // обчислення: MXhMT = MXн * MT
            int[][] MXh = Data.takePartOfMatrixRows(data.MX, data.H * 2, data.H * 3);
            int[][] MXhMT = Data.multiplyMatrixByMatrix(MXh, data.MT);

            //обчислення: VMXhMT = V * MXhMT
            int[] VMXhMT = Data.multiplyMatrixByVector(MXhMT, data.V);
            // a3Zh= a3 * Zн
            int[] a3Zh = Data.multiplyScalarByVector(a3, Data.takePartOfVector(data.Z, data.H * 2, data.H * 3));
            // обчислення : Ан = p * VMXhMT + a3Zh
            int[] Ah = Data.sumOfVectors(
                    Data.multiplyScalarByVector(p3, VMXhMT),
                    a3Zh
            );

            // Запис результату в data.A
            Data.insertSubvectorIntoVector(Ah, data.A, data.H * 2);

            // 17. Сигнал Т4 про завершення обчислення A
            data.S9.release();

            // Повідомлення про завершення виконання потоку T3
            System.out.println("T3 finished");
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}