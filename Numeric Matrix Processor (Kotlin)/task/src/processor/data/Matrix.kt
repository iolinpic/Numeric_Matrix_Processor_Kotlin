package processor.data

import processor.exceptions.WrongInputException
import processor.exceptions.WrongMatrixSizeException

class Matrix(val rows: Int, val cols: Int) {
    val matrix = MutableList(rows) { MutableList<Double>(cols) { 0.0 } }
    fun initMatrixFromInput() {
        for (row in 0 until rows) {
            val line = readln().split(" ").map { it.toDouble() }
            if (line.size < cols) throw WrongInputException()
            for (col in 0 until cols) {
                matrix[row][col] = line[col]
            }
        }

    }

    override fun toString(): String {
        return matrix.joinToString("\n") { it.joinToString(" ") }
    }

    operator fun get(row: Int, col: Int): Double {
        if (row !in 0 until rows || col !in 0 until cols) throw WrongMatrixSizeException()
        return matrix[row][col]
    }

    operator fun set(row: Int, col: Int, value: Double) {
        if (row !in 0 until rows || col !in 0 until cols) throw WrongMatrixSizeException()
        matrix[row][col] = value
    }

    operator fun plus(m: Matrix): Matrix {
        if (rows != m.rows && cols != m.cols) throw WrongMatrixSizeException()
        val res = Matrix(rows, cols)
        for (row in 0 until rows)
            for (col in 0 until cols)
                res[row, col] = this[row, col] + m[row, col]
        return res
    }

    operator fun times(i: Double): Matrix {
        val res = Matrix(rows, cols)
        for (row in 0 until rows)
            for (col in 0 until cols)
                res[row, col] = this[row, col] * i
        return res
    }

    operator fun times(m: Matrix): Matrix {
        if (cols != m.rows) throw WrongMatrixSizeException()
        val res = Matrix(rows, m.cols)
        for (row in 0 until res.rows)
            for (col in 0 until res.cols) {
                res[row, col] = vectorMultiply(this.getRow(row), m.getCol(col))
            }
        return res
    }

    fun transposition(type: String): Matrix {
        if (rows != cols) throw WrongMatrixSizeException()
        val res = Matrix(rows, cols)
        for (row in 0 until rows)
            for (col in 0 until cols)
                res[row, col] = when (type) {
                    "main" -> this[col, row]
                    "side" -> this[cols - 1 - col, rows - 1 - row]
                    "vertical" -> this[row, cols - 1 - col]
                    "horizontal" -> this[rows - 1 - row, col]
                    else -> this[row, col]
                }
        return res
    }

    fun getRow(r: Int): List<Double> {
        return matrix[r]
    }

    fun getCol(c: Int): List<Double> {
        val col = mutableListOf<Double>()
        for (row in 0 until rows) {
            col.add(matrix[row][c])
        }
        return col.toList()
    }

    fun vectorMultiply(a: List<Double>, b: List<Double>): Double {
        if (a.size != b.size) throw Exception("wrong vector sizes")
        var res = 0.0
        for (i in a.indices) {
            res += a[i] * b[i]
        }
        return res
    }

    fun determinant(m: Matrix = this): Double {
        if (m.cols != m.rows) throw WrongMatrixSizeException()
        if (m.cols == 1) return m[0, 0]
        if (m.cols == 2) return m[0, 0] * m[1, 1] - m[0, 1] * m[1, 0]
        var res = 0.0;
        for (col in 0 until m.cols) {
            val sub = generateSubmatrix(m, 0, col)
            val minor = determinant(sub)
            res += countMultiplyer(1, col + 1) * minor * m[0, col]
        }
        return res
    }

    fun generateSubmatrix(m: Matrix, r: Int, c: Int): Matrix {
        if (r !in 0 until m.rows || c !in 0 until m.cols) throw WrongMatrixSizeException()
        val res = Matrix(m.rows - 1, m.cols - 1)
        var rCol = 0
        var rRow = 0
        for (col in 0 until m.cols) {
            for (row in 0 until m.rows) {
                if (col != c && row != r) {
                    res[rRow, rCol] = m[row, col]
                    rRow++
                }
            }
            rRow = 0
            if (col != c) rCol++
        }
        return res
    }

    fun countMultiplyer(r: Int, c: Int): Double {
        return if ((r + c) % 2 == 1) -1.0 else 1.0
    }

    fun inverse(): Matrix {
        val const = 1.0 / determinant()
        val tmp = Matrix(rows, cols)
        for (row in 0 until rows)
            for (col in 0 until cols) {
                val sub = generateSubmatrix(this, row, col)
                val minor = determinant(sub)
                tmp[row, col] = countMultiplyer(row + 1, col + 1) * minor
            }
        return tmp.transposition("main") * const
    }
}