package com.daniorerio;

public class T2 implements Runnable {
    private final int n;
    private final int inputOption;

    public T2(int n, int inputOption) {
        super();
        this.n = n;
        this.inputOption = inputOption;
    }

    @Override
    public void run() {
        // повідомлення про початок виконання потоку T2
        System.out.println("Потік T2 почав виконання");


        // отримання вхідних даних
        int[][] MG = Data.getMatrixOption("MG", n, inputOption, 2);
        int[][] MH = Data.getMatrixOption("MH", n, inputOption, 2);
        int[][] MK = Data.getMatrixOption("MK", n, inputOption, 2);
        int[][] ML = Data.getMatrixOption("ML", n, inputOption, 2);

        int[][] MF = Data.F2(MG, MH, MK, ML);

        // виведення результату
        if (n >= 1000) Data.writeMatrixToFile(MF, "MF");
        else Data.printMatrix(MF, "MF");

        // повідомлення про завершення виконання потоку T2
        System.out.println("Потік T2 завершив виконання");
    }
}