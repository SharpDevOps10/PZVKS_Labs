package com.daniorerio;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Data {
    public int N = 1600;
    public final int P = 4;
    public final int H = N / P;

    // Матриці
    public int[][] MM = new int[N][N];
    public int[][] MX = new int[N][N];
    public int[][] MT = new int[N][N];

    // Вектори
    public int[] A = new int[N];
    public int[] B = new int[N];
    public int[] V = new int[N];
    public int[] Z = new int[N];

    // Скалярні значення
    public int d;
    public int p;

    public AtomicInteger a = new AtomicInteger(0); // A0

    public CyclicBarrier B0 = new CyclicBarrier(P);
    public final Object CS1 = new Object(); // Копіювання a (КД4)
    public final Object CS2 = new Object(); // Копіювання p (КД3)

    public Semaphore S0 = new Semaphore(1);
    public Semaphore S1 = new Semaphore(0);
    public Semaphore S2 = new Semaphore(0);
    public Semaphore S3 = new Semaphore(0);
    public Semaphore S4 = new Semaphore(0);
    public Semaphore S5 = new Semaphore(0);
    public Semaphore S6 = new Semaphore(0);
    public Semaphore S7 = new Semaphore(0);
    public Semaphore S8 = new Semaphore(0);
    public Semaphore S9 = new Semaphore(0);

    public static int[] multiplyScalarByVector(int scalar, int[] vector) {
        int[] result = new int[vector.length];
        for (int i = 0; i < vector.length; i++) {
            result[i] = vector[i] * scalar;
        }
        return result;
    }

    public static int[] multiplyMatrixByVector(int[][] matrix, int[] vector) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        int[] result = new int[rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }

    public static int[] sumOfVectors(int[] vector1, int[] vector2) {
        int[] result = new int[vector1.length];
        for (int i = 0; i < vector1.length; i++) {
            result[i] = vector1[i] + vector2[i];
        }
        return result;
    }

    public static int[][] multiplyMatrixByMatrix(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix2[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    public static int scalarProductOfVectors(int[] vector1, int[] vector2) {
        int result = 0;
        for (int i = 0; i < vector1.length; i++) {
            result += vector1[i] * vector2[i];
        }
        return result;
    }

    public static int[] takePartOfVector(int[] vector, int start, int end) {
        int[] result = new int[end - start];
        System.arraycopy(vector, start, result, 0, end - start);
        return result;
    }

    public static int[][] takePartOfMatrixRows(int[][] matrix, int start, int end) {
        int[][] result = new int[end - start][matrix[0].length];
        for (int i = 0; i < end - start; i++) {
            System.arraycopy(matrix[start + i], 0, result[i], 0, matrix[0].length);
        }
        return result;
    }

    public static void insertSubvectorIntoVector(int[] subvector, int[] vector, int start) {
        System.arraycopy(subvector, 0, vector, start, subvector.length);
    }

    public static int[] getSubvectorFromVector(int[] vector, int start, int length) {
        int[] result = new int[length];
        System.arraycopy(vector, start, result, 0, length);
        return result;
    }

    public static void mergeSort(int[] array, int start, int end) {
        if (start < end) {
            int mid = (start + end) / 2;
            mergeSort(array, start, mid);
            mergeSort(array, mid + 1, end);
            merge(array, start, mid, end);
        }
    }

    public static void merge(int[] array, int start, int mid, int end) {
        int n1 = mid - start + 1;
        int n2 = end - mid;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(array, start, leftArray, 0, n1);
        for (int j = 0; j < n2; ++j) rightArray[j] = array[mid + 1 + j];

        int i = 0, j = 0;
        int k = start;
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}