#include <iostream>
#include <stdexcept>
#include <vector>
#include "utils.hpp"

namespace LinearAlgebraUtils {
  // Додавання векторів
  std::vector<int> addVectors(const std::vector<int> &a, const std::vector<int> &b) {
    if (a.size() != b.size()) {
      throw std::invalid_argument("Vectors must be of the same length");
    }
    std::vector<int> result(a.size());
#pragma omp parallel for
    for (size_t i = 0; i < a.size(); ++i) {
      result[i] = a[i] + b[i];
    }
    return result;
  }

  // Віднімання векторів
  std::vector<int> subtractVectors(const std::vector<int> &a, const std::vector<int> &b) {
    if (a.size() != b.size()) {
      throw std::invalid_argument("Vectors must be of the same length");
    }
    std::vector<int> result(a.size());
#pragma omp parallel for
    for (size_t i = 0; i < a.size(); ++i) {
      result[i] = a[i] - b[i];
    }
    return result;
  }

  // Множення вектора на скаляр
  std::vector<int> scalarMultiplyVector(const std::vector<int> &a, int scalar) {
    std::vector<int> result(a.size());
#pragma omp parallel for
    for (size_t i = 0; i < a.size(); ++i) {
      result[i] = a[i] * scalar;
    }
    return result;
  }

  // Скалярний добуток векторів
  int dotProduct(const std::vector<int> &a, const std::vector<int> &b) {
    if (a.size() != b.size()) {
      throw std::invalid_argument("Vectors must be of the same length");
    }
    int result = 0;
#pragma omp parallel for reduction(+ : result)
    for (size_t i = 0; i < a.size(); ++i) {
      result += a[i] * b[i];
    }
    return result;
  }

  // Додавання матриць
  std::vector<std::vector<int>> addMatrices(const std::vector<std::vector<int>> &a,
                                            const std::vector<std::vector<int>> &b) {
    if (a.size() != b.size() || a[0].size() != b[0].size()) {
      throw std::invalid_argument("Matrices must be of the same dimensions");
    }
    std::vector result(a.size(), std::vector<int>(a[0].size()));
#pragma omp parallel for collapse(2) schedule(static)
    for (size_t i = 0; i < a.size(); ++i) {
      for (size_t j = 0; j < a[0].size(); ++j) {
        result[i][j] = a[i][j] + b[i][j];
      }
    }
    return result;
  }

  // Віднімання матриць
  std::vector<std::vector<int>> subtractMatrices(const std::vector<std::vector<int>> &a,
                                                 const std::vector<std::vector<int>> &b) {
    if (a.size() != b.size() || a[0].size() != b[0].size()) {
      throw std::invalid_argument("Matrices must be of the same dimensions");
    }
    std::vector result(a.size(), std::vector<int>(a[0].size()));
#pragma omp parallel for collapse(2) schedule(static)
    for (size_t i = 0; i < a.size(); ++i) {
      for (size_t j = 0; j < a[0].size(); ++j) {
        result[i][j] = a[i][j] - b[i][j];
      }
    }
    return result;
  }

  // Множення матриці на скаляр
  std::vector<std::vector<int>> scalarMultiplyMatrix(const std::vector<std::vector<int>> &a, int scalar) {
    std::vector result(a.size(), std::vector<int>(a[0].size()));
#pragma omp parallel for
    for (size_t i = 0; i < a.size(); ++i) {
      for (size_t j = 0; j < a[0].size(); ++j) {
        result[i][j] = a[i][j] * scalar;
      }
    }
    return result;
  }

  // Множення матриць
  std::vector<std::vector<int>> multiplyMatrices(const std::vector<std::vector<int>> &a,
                                                 const std::vector<std::vector<int>> &b) {
    if (a[0].size() != b.size()) {
      throw std::invalid_argument("Number of columns of first matrix must be equal to number of rows of second matrix");
    }
    std::vector result(a.size(), std::vector(b[0].size(), 0));
#pragma omp parallel for collapse(2) schedule(static)
    for (size_t i = 0; i < a.size(); ++i) {
      for (size_t j = 0; j < b[0].size(); ++j) {
        for (size_t k = 0; k < a[0].size(); ++k) {
          result[i][j] += a[i][k] * b[k][j];
        }
      }
    }
    return result;
  }

  // Транспонування матриці
  std::vector<std::vector<int>> transposeMatrix(const std::vector<std::vector<int>> &matrix) {
    std::vector result(matrix[0].size(), std::vector<int>(matrix.size()));
#pragma omp parallel for
    for (size_t i = 0; i < matrix.size(); ++i) {
      for (size_t j = 0; j < matrix[0].size(); ++j) {
        result[j][i] = matrix[i][j];
      }
    }
    return result;
  }

  // Заповнення вектора одиницями
  void fillVector(std::vector<int> &vector) {
#pragma omp parallel for
    for (size_t i = 0; i < vector.size(); ++i) {
      vector[i] = 1;
    }
  }

  // Заповнення матриці одиницями
  void fillMatrix(std::vector<std::vector<int>> &matrix) {
#pragma omp parallel for
    for (size_t i = 0; i < matrix.size(); ++i) {
      std::fill(matrix[i].begin(), matrix[i].end(), 1);
    }
  }

  // Відділення частини вектора
  std::vector<int> divideVector(const std::vector<int> &vector, const int part, const int H) {
    const int start = part * H;
    const int end = (part == (vector.size() / H) - 1) ? vector.size() : start + H;
    return std::vector(vector.begin() + start, vector.begin() + end);
  }

  // Відділення частини матриці
  std::vector<std::vector<int>> divideMatrix(const std::vector<std::vector<int>> &matrix, const int part, const int H) {
    const int start = part * H;
    const int end = (part == (matrix.size() / H) - 1) ? matrix.size() : start + H;
    return std::vector(matrix.begin() + start, matrix.begin() + end);
  }

  // Збирання частини матриці в загальну матрицю
  void assembleMatrix(std::vector<std::vector<int>> &resultMatrix, const std::vector<std::vector<int>> &matrixPart,
                      const int part) {
    const int partColumns = matrixPart[0].size();
#pragma omp parallel for
    for (size_t i = 0; i < resultMatrix.size(); ++i) {
      for (size_t j = 0; j < partColumns; ++j) {
        resultMatrix[i][part * partColumns + j] = matrixPart[i][j];
      }
    }
  }

  void printMatrix(const std::vector<std::vector<int>> &matrix) {
    for (const auto &row: matrix) {
      for (const auto &value: row) {
        printf("%d ", value);
      }
      printf("\n");
    }
  }
} // namespace LinearAlgebraUtils
