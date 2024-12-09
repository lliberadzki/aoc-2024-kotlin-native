@file:OptIn(ExperimentalForeignApi::class)

import kotlinx.cinterop.*
import platform.posix.*

fun main() {
    println(read())
}

fun read(): Any {
    return input
}

private val input: String by lazy { readInput()}

fun countOccurrences(str: String, searchStr: String): Int {
    var count = 0
    var startIndex = 0

    while (startIndex < str.length) {
        val index = str.indexOf(searchStr, startIndex)
        if (index >= 0) {
            count++
            startIndex = index + searchStr.length
        } else {
            break
        }
    }

    return count
}

fun transpose(puzzle: List<String>): List<String> {
    var transposed = ArrayList<String>()
    for (i in 0..<puzzle[0].length) {
        transposed.add(puzzle.map { it[i] }.joinToString(""))
    }
    return transposed
}

fun diagonals(puzzle: List<String>): List<String> {
    var diagonals = ArrayList<String>()
    var word: String
    for (base in puzzle.indices) {
        word = ""
        for (i in 0..<puzzle.size-base) {
            word += puzzle[base+i][i]
        }
        diagonals.add(word)
    }
    for (base in 1..<puzzle.size) {
        word = ""
        for (i in 0..<puzzle.size-base) {
            word += puzzle[i][base+i]
        }
        diagonals.add(word)
    }
    return diagonals
}

@OptIn(ExperimentalForeignApi::class)
private fun readInput(): String {
    val cwd = getcwd(null, 0U)?.toKString()
    val resourceDir = "${cwd}/src/commonMain/resources"
    val file = fopen("$resourceDir/Day4.input", "r") ?:
    throw IllegalArgumentException("Cannot open input file Day4.input")

    var puzzle = ArrayList<String>()
    var acc = 0

    try {
        memScoped {
            val readBufferLength = 64 * 1024
            val buffer = allocArray<ByteVar>(readBufferLength)
            var line = fgets(buffer, readBufferLength, file)?.toKString()
            while (line != null) {
                // operate on line
                puzzle.add(line.trim())
                line = fgets(buffer, readBufferLength, file)?.toKString()
            }
        }

        var size = puzzle.size
        for (i in 1..<size-1) {
            for (j in 1..<size-1) {
                if (puzzle[j][i] != 'A') {
                    continue
                }

                if ((puzzle[j-1][i-1] == 'M' && puzzle[j+1][i+1] == 'S') ||
                    (puzzle[j+1][i+1] == 'M' && puzzle[j-1][i-1] == 'S')) {
                    if ((puzzle[j-1][i+1] == 'M' && puzzle[j+1][i-1] == 'S') ||
                        (puzzle[j+1][i-1] == 'M' && puzzle[j-1][i+1] == 'S')) {
                        acc++
                    } else {
                        continue
                    }
                } else if ((puzzle[j-1][i+1] == 'M' && puzzle[j+1][i-1] == 'S') ||
                    (puzzle[j+1][i-1] == 'M' && puzzle[j-1][i+1] == 'S')) {
                    if ((puzzle[j-1][i-1] == 'M' && puzzle[j+1][i+1] == 'S') ||
                        (puzzle[j+1][i+1] == 'M' && puzzle[j-1][i-1] == 'S')) {
                        acc++
                    } else {
                        continue
                    }
                }
            }
        }

        println(acc)
    } finally {
        fclose(file)
    }

    return ""
}
