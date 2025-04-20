#ifndef UTILS_H
#define UTILS_H
#include <vector>

namespace LinearAlgebraUtils {
  // Додавання векторів
  std::vector<int> addVectors(const std::vector<int> &a, const std::vector<int> &b);

  // Віднімання векторів
  std::vector<int> subtractVectors(const std::vector<int> &a, const std::vector<int> &b);

  // Множення вектора на скаляр
  std::vector<int> scalarMultiplyVector(const std::vector<int> &a, int scalar);

  // Скалярний добуток двох векторів
  int dotProduct(const std::vector<int> &a, const std::vector<int> &b);

  // Додавання матриць
  std::vector<std::vector<int> > addMatrices(const std::vector<std::vector<int> > &a,
                                             const std::vector<std::vector<int> > &b);

  // Віднімання матриць
  std::vector<std::vector<int> > subtractMatrices(const std::vector<std::vector<int> > &a,
                                                  const std::vector<std::vector<int> > &b);

  // Множення матриці на скаляр
  std::vector<std::vector<int> > scalarMultiplyMatrix(const std::vector<std::vector<int> > &a, int scalar);

  // Множення матриць
  std::vector<std::vector<int> > multiplyMatrices(const std::vector<std::vector<int> > &a,
                                                  const std::vector<std::vector<int> > &b);

  // Транспонування матриці
  std::vector<std::vector<int> > transposeMatrix(const std::vector<std::vector<int> > &matrix);

  // Заповнення вектора одиницями
  void fillVector(std::vector<int> &vector);

  // Заповнення матриці одиницями
  void fillMatrix(std::vector<std::vector<int> > &matrix);

  // Розбиття вектора на частини
  std::vector<int> divideVector(const std::vector<int> &vector, int part, int H);

  // Розбиття матриці на частини
  std::vector<std::vector<int> > divideMatrix(const std::vector<std::vector<int> > &matrix, int part, int H);

  // Збирання матриці з частин
  void assembleMatrix(std::vector<std::vector<int> > &resultMatrix, const std::vector<std::vector<int> > &matrixPart,
                      int part);

  // Виведення матриці на екран
  void printMatrix(const std::vector<std::vector<int> > &matrix);
} // namespace LinearAlgebraUtils

#endif
