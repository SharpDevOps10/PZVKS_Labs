/**
 * ПЗВПКС
 * Лабораторна робота ЛР2.2
 * Семафори, критичні секції, атомік-змінні, бар’єри в мові Java
 * Варіант 16
 * A= p *(sort(d*B + Z*MM) * (MX*MT) )+ (B*Z)*Z
 * Тимофеєв Даниіл Костянтинович
 * Група ІМ-22
 * Дата 24.03.2025
 */

package com.daniorerio;

public class Lab2 {
    public static void main(String[] args) {
        // Створення екземпляра класу Data
        Data data = new Data();

        // Створення потоків
        Thread t1 = new T1(data);
        Thread t2 = new T2(data);
        Thread t3 = new T3(data);
        Thread t4 = new T4(data);

        // Запуск потоків
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}