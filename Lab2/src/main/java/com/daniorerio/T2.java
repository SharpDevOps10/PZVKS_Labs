package com.daniorerio;

import java.util.concurrent.BrokenBarrierException;

public class T2 extends Thread {
    private final Data data;
    int d2;
    int p2;
    int a2;

    public T2(Data data) {
        this.data = data;
    }

    @Override
    public void run() {
        try {
            // Повідомлення про початок виконання потоку Т2
            System.out.println("T2 started");

            // 1. Чекаємо на введення даних у T1, T3, T4
            data.B0.await();

            //  2. Копія d2 = d (КД1)
            data.S0.acquire();
            d2 = data.d;
            data.S0.release();

            // 3. Обчислення Vн = sort(d2 * Bн + Z * MMн)
            // обчислення: Vh_part1 = d2 * Bн
            int[] Vh_part1 = Data.multiplyScalarByVector(d2, Data.takePartOfVector(data.B, data.H, data.H * 2));

            // обчислення: Vh_part2 = Z * MMн
            int[] Vh_part2 = Data.multiplyMatrixByVector(
                    Data.takePartOfMatrixRows(data.MM, data.H, data.H * 2),
                    data.Z
            );

            // обчислення 1: Vh = sort(Vh_part1 + Vh_part2)
            int[] Vh = Data.sumOfVectors(Vh_part1, Vh_part2);
            Data.insertSubvectorIntoVector(Vh, data.V, data.H);
            Data.mergeSort(data.V, data.H, data.H * 2 - 1);

            // 4. Сигнал задачі Т1 про завершення обчислення Vн
            data.S2.release();

            // 5. Чекати на завершення обчислення V у Т1
            data.S1.acquire();

            // 6. Обчислення 2: a2 = (Bн * Zн)
            a2 = Data.scalarProductOfVectors(
                    Data.getSubvectorFromVector(data.B, data.H, data.H),
                    Data.getSubvectorFromVector(data.Z, data.H, data.H)
            );

            //  7. КД2: захищений запис a = a + a2
            data.a.updateAndGet(a -> a + a2);

            // 8. Сигнал потокам Т1, Т3, Т4 про завершення обчислення
            data.S6.release(3);

            //  9. Чекаємо на завершення обчислення a в інших потоках
                // Очікування завершення обчислення потоком Т1
            data.S5.acquire();
                // Очікування завершення обчислення потоком Т3
            data.S7.acquire();
                // Очікування завершення обчислення потоком Т4
            data.S8.acquire();

            //  10. КД3: захищене копіювання p
            synchronized (data.CS2) {
                p2 = data.p;
            }

            //  11. КД4: захищене копіювання a
            synchronized (data.CS1) {
                a2 = data.a.get();
            }

            //  12. Обчислення Ан = p2 * V * (MXн * MT) + a2 * Zн
            // обчислення: MXhMT = MXн * MT
            int[][] MXh = Data.takePartOfMatrixRows(data.MX, data.H, data.H * 2);
            int[][] MXhMT = Data.multiplyMatrixByMatrix(MXh, data.MT);

            //обчислення: VMXhMT = V * MXhMT
            int[] VMXhMT = Data.multiplyMatrixByVector(MXhMT, data.V);

            // a2Zh= a2 * Zн
            int[] a2Zh = Data.multiplyScalarByVector(a2,
                    Data.takePartOfVector(data.Z, data.H, data.H * 2));

            // обчислення : Ан = p * VMXhMT + a2Zh
            int[] Ah = Data.sumOfVectors(
                    Data.multiplyScalarByVector(p2, VMXhMT),
                    a2Zh
            );
            // Запис результату Ан в data.A
            Data.insertSubvectorIntoVector(Ah, data.A, data.H);

            //  13. Сигнал Т4 про завершення обчислення A
            data.S9.release();

            // Повідомлення про завершення виконання потоку T2
            System.out.println("T2 finished");
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}