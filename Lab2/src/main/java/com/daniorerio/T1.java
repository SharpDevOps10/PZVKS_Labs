package com.daniorerio;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;

public class T1 extends Thread {
    private final Data data;
    int d1;
    int p1;
    int a1;

    public T1(Data data) {
        this.data = data;
    }

    @Override
    public void run() {
        try {
            // Повідомлення про початок виконання потоку Т1
            System.out.println("T1 started");

            // заповнення MM
            for (int i = 0; i < data.N; i++) {
                for (int j = 0; j < data.N; j++) {
                    data.MM[i][j] = 1;
                }
            }

            Arrays.fill(data.B, 1);
            // заповнення B різними значеннями для демонстрації сортування
            data.B[0] = 4;
            data.B[1] = 3;
            data.B[2] = 2;

            // заповнення MX
            for (int i = 0; i < data.N; i++) {
                for (int j = 0; j < data.N; j++) {
                    data.MX[i][j] = 1;
                }
            }

            // Сигнал потокам Т1, Т3, Т4 про завершення заповнення даних
            // Очікування завершення заповнення даних потоками Т2, Т4
            data.B0.await();

            // копіювання d1 = d (КД1) S0
            data.S0.acquire();
            d1 = data.d;
            data.S0.release();

            // обчислення: Vh_part1 = d1 * Bн
            int[] Vh_part1 = Data.multiplyScalarByVector(d1, Data.takePartOfVector(data.B, 0, data.H));

            // обчислення: Vh_part1 = Z * MMн
            int[] Vh_part2 = Data.multiplyMatrixByVector(
                    Data.takePartOfMatrixRows(data.MM, 0, data.H),
                    data.Z
            );

            // обчислення 1: Vн = sort(Vh_part1 + Vh_part2)
            int[] Vh = Data.sumOfVectors(Vh_part1, Vh_part2);
            Data.insertSubvectorIntoVector(Vh, data.V, 0);
            Data.mergeSort(data.V, 0, data.H - 1);

            // Чекати на завершення обчислення Vн в Т2
            data.S2.acquire();

            // Перевірка правильності сортування Vн N <= 16
            if (data.N <= 16) System.out.println("T1: Each quarter sorted: " + Arrays.toString(data.V));

            // обчислення 2: V2h = merge(Vн, Vн)
            Data.merge(data.V, 0, data.H - 1, data.H * 2 - 1);

            // Чекати на завершення обчислення V2н у Т3
            data.S3.acquire();

            // Перевірка правильності сортування V2н N <= 16
            if (data.N <= 16) System.out.println("T1: Each half sorted: " + Arrays.toString(data.V));

            // обчислення 3: V= merge(V2н, V2н),
            Data.merge(data.V, 0, data.H * 2 - 1, data.H * 4 - 1);

            // Перевірка правильності сортування V N <= 16
            if (data.N <= 16) System.out.println("T1: Fully sorted vector: " + Arrays.toString(data.V));

            // Сигнал потокам Т2, Т3, Т4 про завершення обчислень
            data.S1.release(3);

            // обчислення 4: a1 = (Bн * Zн)
            a1 = Data.scalarProductOfVectors(
                    Data.getSubvectorFromVector(data.B, 0, data.H),
                    Data.getSubvectorFromVector(data.Z, 0, data.H)
            );

            // КД2: захищений запис a = a + ai
            // обчислення 5: а = а + a1 (КД2)
            data.a.updateAndGet(a -> a + a1);

            // Сигнал потокам Т2, Т3, Т4 про завершення обчислень
            data.S5.release(3); // Для T2, T3, T4

            // Очікування завершення обчислення a в інших потоках
            data.S6.acquire(); // T2
            data.S7.acquire(); // T3
            data.S8.acquire(); // T4

            // КД3: захищене копіювання p
            synchronized (data.CS2) {
                p1 = data.p;
            }

            // КД4: захищене копіювання a
            synchronized (data.CS1) {
                a1 = data.a.get();
            }

            // ------------------------ Обчислення Ан ------------------------
            // обчислення: MXhMT = MXн * MT
            int[][] MXh = Data.takePartOfMatrixRows(data.MX, 0, data.H);
            int[][] MXhMT = Data.multiplyMatrixByMatrix(MXh, data.MT);

            //обчислення: VMXhMT = V * MXhMT
            int[] VMXhMT = Data.multiplyMatrixByVector(MXhMT, data.V);

            // a1Zh= a1 * Zн
            int[] a1Zh = Data.multiplyScalarByVector(a1, Data.takePartOfVector(data.Z, 0, data.H));

            // обчислення 6: Ан = p * VMXhMT + a1Zh
            int[] Ah = Data.sumOfVectors(
                    Data.multiplyScalarByVector(p1, VMXhMT),
                    a1Zh
            );

            // Запис результату в data.A
            Data.insertSubvectorIntoVector(Ah, data.A, 0);

            // Сигнал задачі Т4 про завершення обчислення A
            data.S9.release();

            // Повідомлення про завершення виконання потоку Т1
            System.out.println("T1 finished");

        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}