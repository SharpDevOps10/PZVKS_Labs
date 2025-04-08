package com.daniorerio;

import java.util.Arrays;

public class Data {
    public static int[] Z = new int[Lab3.N];
    public static int[][] MD = new int[Lab3.N][Lab3.N];
    public static int[][] MC = new int[Lab3.N][Lab3.N];
    public static int[][] MX = new int[Lab3.N][Lab3.N];
    public static int[][] MO = new int[Lab3.N][Lab3.N];

    // Заповнення скаляра
    public static int readScalar() {
        return 1;
    }

    // Заповнення вектора
    public static void readVector(int[] vector) {
        Arrays.fill(vector, 1);
    }

    // Заповнення матриці
    public static void readMatrix(int[][] matrix) {
        for (int[] vector : matrix) {
            Arrays.fill(vector, 1);
        }
    }

    // Часткове обчислення максимуму у векторі
    public static int partiallyMaxInVector(int[] vector, int startIndex) {
        int max = vector[startIndex];
        for (int i = startIndex; i < startIndex + Lab3.H; i++) {
            if (vector[i] > max) max = vector[i];
        }
        return max;
    }

    // Часткове множення матриць
    public static int[][] partiallyMultiplyMatrices(int[][] matrixA, int[][] matrixB, int startIndex) {
        int rowsA = matrixA.length;
        int colsA = matrixA[0].length;

        int[][] result = new int[rowsA][colsA];

        for (int i = 0; i < rowsA; i++) {
            for (int j = startIndex; j < startIndex + Lab3.H; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }

        return result;
    }

    // Часткове множення матриці на скаляр
    public static int[][] partiallyMultiplyMatrixScalar(int[][] matrix, int scalar, int startIndex) {
        int[][] result = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = startIndex; j < startIndex + Lab3.H; j++) {
                result[i][j] = matrix[i][j] * scalar;
            }
        }
        return result;
    }

    // Часткове додавання матриць
    public static void partiallyAddMatrices(int[][] matrix1, int[][] matrix2, int startIndex, int[][] result) {
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = startIndex; j < startIndex + Lab3.H; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
    }

    // Виведення результату
    public static void printResultMatrix(String threadName, int[][] matrix) {
        System.out.println("Result matrix for " + threadName + ": ");
        for (int[] ints : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(ints[j] + " ");
            }
            System.out.println();
        }
    }
}