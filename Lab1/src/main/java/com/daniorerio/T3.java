package com.daniorerio;

public class T3 implements Runnable {
    private final int n;
    private final int inputOption;


    public T3(int n, int inputOption) {
        super();
        this.n = n;
        this.inputOption = inputOption;
    }

    @Override
    public void run() {
        // повідомлення про початок виконання потоку T3
        System.out.println("Потік T3 почав виконання");

        // отримання вхідних даних
        int[] P = Data.getVectorOption("P", n, inputOption, 3);
        int[][] MR = Data.getMatrixOption("MR", n, inputOption, 3);
        int[][] MT = Data.getMatrixOption("MT", n, inputOption, 3);

        // обчислення функції F3
        int[] O = Data.F3(P, MR, MT);

        // виведення результату
        if (n >= 1000) Data.writeVectorToFile(O, "O");
        else Data.printVector(O, "O");

        // повідомлення про завершення виконання потоку T3
        System.out.println("Потік T3 завершив виконання");
    }
}
