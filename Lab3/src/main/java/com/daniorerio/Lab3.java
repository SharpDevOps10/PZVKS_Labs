/**
 * ПЗВПКС
 * Переробка Лабораторна робота ЛР3.2
 * Монітори в мові Java
 * Варіант 28
 * МO = (MD*MC)*e + max(Z)*MX
 * Тимофеєв Даниіл Костянтинович
 * Група ІМ-22
 * Дата 06.04.2025
 */

package com.daniorerio;

public class Lab3 {
    public static int N = 1200;
    public static int P = 4;
    public static int H = Lab3.N / Lab3.P;

    public static void main(String[] args) {
        // Збереження інформації про час початку програми
        long startTime = System.currentTimeMillis();

        // Створення екземпляра класу Monitor (монітора)
        Monitor monitor = new Monitor();

        // Створення потоків
        T1 T1 = new T1(monitor);
        T2 T2 = new T2(monitor);
        T3 T3 = new T3(monitor);
        T4 T4 = new T4(monitor);

        // Запуск потоків
        T1.start();
        T2.start();
        T3.start();
        T4.start();

        // Очікування завершення роботи дочірніх потоків головним потоком
        try {
            T1.join();
            T2.join();
            T3.join();
            T4.join();
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        // Збереження інформації про час завершення програми
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // Показ інформації про витрачений час
        System.out.println("Time taken: " + (elapsedTime / 1000.0) + " seconds");
    }
}