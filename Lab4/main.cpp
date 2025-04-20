/**
 * ПЗВПКС
 * Лабораторна робота ЛР4
 * Бібліотека OpenMP. Бар'єри, критичні секції
 * Варіант 18
 * MX= (B*Z)*(MZ*MM) - (MR*MC)*d
 * Тимофеєв Даниіл Костянтинович
 * Група ІМ-22
 * Дата 19.04.2025
 */

#include <chrono>
#include <cstdio>
#include <iostream>
#include <omp.h>
#include "utils.hpp"

using namespace std;
using namespace LinearAlgebraUtils;

constexpr int N = 2000;
constexpr int P = 4;
constexpr int H = N / P;

// Структура для збереження глобальних даних
struct GlobalData {
  // Вхідні дані
  vector<vector<int> > MZ;
  vector<vector<int> > MM;
  vector<vector<int> > MR;
  vector<vector<int> > MC;
  vector<int> B;
  vector<int> Z;
  int d{};
  // Результуюча матриця
  vector<vector<int> > MX = vector(N, vector(N, 0));
  int a = 0;
};

int main() {
  GlobalData globalData;
  omp_set_nested(1);

  // Початок відліку часу виконання
  const auto start = std::chrono::high_resolution_clock::now();

  // Створення потоків та їхній запуск
#pragma omp parallel num_threads(P) shared(globalData)
  {
    const int tid = omp_get_thread_num(); // Отримання ID потоку

    // Вивід інформації про початок роботи потоку
#pragma omp critical
    {
      cout << "Потік T" << tid + 1 << " Почав виконання\n";
    }

    // Введення вхідних даних (Z, d, MM, MR, B, MC, MZ)
    switch (tid) {
      case 0: // Потік T1: введення Z, d
        globalData.d = 1;
        globalData.Z.resize(N);
        fillVector(globalData.Z);
        break;
      case 1: // Потік Т2: введення MM
        globalData.MM.resize(N, vector<int>(N));
        fillMatrix(globalData.MM);
        break;
      case 2: // Потік Т3: введення MR, B
        globalData.B.resize(N);
        globalData.MR.resize(N, vector<int>(N));
        fillVector(globalData.B);
        fillMatrix(globalData.MR);
        break;
      case 3: // Потік Т4: введення MC, MZ
        globalData.MC.resize(N, vector<int>(N));
        globalData.MZ.resize(N, vector<int>(N));
        fillMatrix(globalData.MC);
        fillMatrix(globalData.MZ);
        break;
      default:
        break;
    }

    // Бар'єр B1 для синхронізації з введення даних
#pragma omp barrier
    // Обчислення 1: ai = ( Bн * Zн)
    const vector<int> localB = divideVector(globalData.B, tid, H);
    const vector<int> localZ = divideVector(globalData.Z, tid, H);
    const int ai = dotProduct(localB, localZ);

    // критична секція CS1 - для обчислення спільного ресурсу a (КД1)
#pragma omp critical(CS1)
    {
      globalData.a += ai; // Обчислення 2: a = a + ai
    }

    // бар’єр B2 - для синхронізації виконання потоків після обчислення значення скаляра a
#pragma omp barrier
    const vector<vector<int> > localMM = divideMatrix(globalData.MM, tid, H);
    const vector<vector<int> > localMC = divideMatrix(globalData.MC, tid, H);

    int aCopy, dCopy;

    // критична секція CS2 - для копіювання спільного ресурсу a (КД2)
#pragma omp critical(CS2)
    {
      aCopy = globalData.a; // Копія a (КД2)
    }

    // критична секція CS3 - для копіювання спільного ресурсу d (КД3)
#pragma omp critical(CS3)
    {
      dCopy = globalData.d; // Копія d (КД3)
    }

    // Обчислення 3 (частини MX) MXh = a * (MZ * MMh) - (MR * MCh) * d
    const vector<vector<int> > localMX =
        subtractMatrices(scalarMultiplyMatrix(multiplyMatrices(globalData.MZ, transposeMatrix(localMM)), aCopy),
                         scalarMultiplyMatrix(multiplyMatrices(globalData.MR, transposeMatrix(localMC)), dCopy));

    // Збирання результату у глобальну матрицю MX
    assembleMatrix(globalData.MX, localMX, tid);

    // бар’єр B3 - для синхронізації по обчисленню результуючої матриці MX потоками Т1, T2, Т3, Т4
#pragma omp barrier
    // Виведення інформації про завершення виконання потоку
#pragma omp critical
    {
      // Виведення результату
      if (tid == 1) {
        // Потік T2
        printf("Результуюча матриця (MX):\n");
        printMatrix(globalData.MX);
      }

      printf("Потік Т%d закінчив своє виконання\n", tid + 1);
    }
  }

  // Виведення часу виконання
  const auto end = std::chrono::high_resolution_clock::now();
  const std::chrono::duration<double> duration = end - start;

  cout << "Час виконання програми: " << duration.count() << " секунд\n";

  return 0;
}
