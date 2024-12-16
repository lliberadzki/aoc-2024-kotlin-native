@file:OptIn(ExperimentalForeignApi::class)

import kotlinx.cinterop.*
import platform.posix.*

fun main() {
    println(read())
}

fun read(): Any {
    return input
}

private val input: String by lazy { readInput() }

fun printCharArray(puzzle: List<CharArray>) {
    println("puzzle:")
    println(puzzle.map { it.joinToString("") }.joinToString("\n"))

}

fun findRoute(puzzle: List<CharArray>, startX: Int, startY: Int): Boolean {
    val max = puzzle.size
    val maxSteps = max * max

    var x = startX
    var y = startY
    var prevX = startX
    var prevY = startY
    var dir = 0
    var steps = 0

    puzzle[x][y] = 'X'
    while (x >= 0 && y >= 0 && x < max && y < max && steps < maxSteps) {
        if (puzzle[x][y] == '#') {
            dir++
            dir %= 4
            x = prevX
            y = prevY
        } else {
            prevX = x
            prevY = y
            puzzle[x][y] = 'X'
            steps++
        }

        when (dir) {
            0 -> x--
            1 -> y++
            2 -> x++
            3 -> y--
        }
    }

    return steps != maxSteps
}

fun testLoop(puzzle: List<CharArray>, startX: Int, startY: Int): Boolean {
    val max = puzzle.size
    val maxSteps = max * max

    var x = startX
    var y = startY
    var prevX = startX
    var prevY = startY
    var dir = 0
    var steps = 0

    puzzle[x][y] = '1'
    while (x >= 0 && y >= 0 && x < max && y < max && steps < maxSteps) {
        if (puzzle[x][y] == '#') {
            dir++
            dir %= 4
            x = prevX
            y = prevY
        } else {
            prevX = x
            prevY = y
            if (puzzle[x][y] == dir.toChar()) {
                return false
            } else {
                puzzle[x][y] = dir.toChar()
            }
            steps++
        }

        when (dir) {
            0 -> x--
            1 -> y++
            2 -> x++
            3 -> y--
        }
    }

    return steps != maxSteps
}

@OptIn(ExperimentalForeignApi::class)
private fun readInput(): String {
    val cwd = getcwd(null, 0U)?.toKString()
    val resourceDir = "${cwd}/src/commonMain/resources"
    val file = fopen("$resourceDir/Day6.input", "r") ?:
    throw IllegalArgumentException("Cannot open input file Day6.input")

    val puzzle: ArrayList<CharArray> = ArrayList()
    var x: Int = -1
    var y: Int = -1
    var indexOf: Int

    try {
        memScoped {
            val readBufferLength = 64 * 1024
            val buffer = allocArray<ByteVar>(readBufferLength)
            var line = fgets(buffer, readBufferLength, file)?.toKString()
            while (line != null) {
                puzzle.add(line.trim().toCharArray())
                indexOf = line.indexOf("^")
                if (indexOf > -1) {
                    x = puzzle.size - 1
                    y = indexOf
                }

                line = fgets(buffer, readBufferLength, file)?.toKString()
            }
        }

        //printCharArray(puzzle)
        //println(startingPoint)

        val originalRoutePuzzle = puzzle.map { it.copyOf() }
        findRoute(originalRoutePuzzle, x, y)

        var acc1 = 1
        var acc2 = 0
        val size = originalRoutePuzzle.size
        for (i in 0..<size) {
            for (j in 0..<size) {
                if (originalRoutePuzzle[i][j] == 'X' && !(i == x && j == y)) {
                    acc1++
                    val newPuzzle = puzzle.map { it.copyOf() }
                    newPuzzle[i][j] = '#'
                    if (!testLoop(newPuzzle, x, y)) {
                        acc2++
                    }
                }
            }
        }
        println(acc1)
        println(acc2)

    } finally {
        fclose(file)
    }

    return ""
}
