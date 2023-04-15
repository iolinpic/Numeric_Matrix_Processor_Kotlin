package processor.services

import processor.data.Matrix
import processor.exceptions.WrongInputException
import processor.exceptions.WrongMatrixSizeException

class ActionsService {
    fun runMenu() {
        while (true) {
            val action = printMenu()
            try {
                when (action) {
                    0 -> break
                    1 -> sumAction()
                    2 -> constantMultiplyAction()
                    3 -> matrixMultiplyAction()
                    4 -> transpositionAction()
                    5 -> determenantAction()
                    6 -> inverseAction()
                }
            } catch (ex: WrongInputException) {
                println(ex.message)
            } catch (ex: WrongMatrixSizeException) {
                println(ex.message)
            } catch (err: Exception) {
                println(err.message)
            }
        }

    }

    fun readMatrix(num: String = ""): Matrix {
        println("Enter size of$num matrix:")
        val line = readln().split(" ").map { it.toInt() }
        if (line.size < 2) throw WrongInputException()
        val m = Matrix(line[0], line[1])
        println("Enter$num matrix:")
        m.initMatrixFromInput()
        return m
    }

    fun sumAction() {
        val m1 = readMatrix(" first")
        val m2 = readMatrix(" second")
        val res = m1 + m2
        println("The result is:")
        println(res)
    }

    fun constantMultiplyAction() {
        val m1 = readMatrix()
        val const = readln().toDouble()
        val res = m1 * const
        println("The result is:")
        println(res)
    }

    fun matrixMultiplyAction() {
        val m1 = readMatrix(" first")
        val m2 = readMatrix(" second")
        val res = m1 * m2
        println("The result is:")
        println(res)
    }

    fun transpositionAction() {
        println("1. Main diagonal")
        println("2. Side diagonal")
        println("3. Vertical line")
        println("4. Horizontal line")
        val action = readln().toInt()
        if (action !in 1..4) return

        val m = readMatrix()

        val res = when (action) {
            1 -> m.transposition("main")
            2 -> m.transposition("side")
            3 -> m.transposition("vertical")
            4 -> m.transposition("horizontal")
            else -> {
                throw WrongInputException()
            }
        }
        println("The result is:")
        println(res)
    }

    fun determenantAction() {
        val m = readMatrix()
        val res = m.determinant()
        println("The result is:")
        println(res)
    }
    fun inverseAction(){
        val m = readMatrix()
        val res = m.inverse()
        println("The result is:")
        println(res)
    }

    fun printMenu(): Int {
        println("1. Add matrices")
        println("2. Multiply matrix by a constant")
        println("3. Multiply matrices")
        println("4. Transpose matrix")
        println("5. Calculate a determinant")
        println("6. Inverse matrix")
        println("0. Exit")
        println("Your choice: ")
        return readln().toInt()
    }
}