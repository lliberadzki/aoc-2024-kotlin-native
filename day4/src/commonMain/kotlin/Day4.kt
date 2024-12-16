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

@OptIn(ExperimentalForeignApi::class)
private fun readInput(): String {
    val cwd = getcwd(null, 0U)?.toKString()
    val resourceDir = "${cwd}/src/commonMain/resources"
    val file = fopen("$resourceDir/Day4.input", "r") ?:
    throw IllegalArgumentException("Cannot open input file Day4.input")

    var puzzle = ArrayList<String>()
    var acc1 = 0
    var acc2 = 0

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
        // part 1
        for (i in 0..<size) {
            for (j in 0..<size) {
                if (puzzle[j][i] != 'X') {
                    continue
                }

                if (j > 2) {
                    if (puzzle[j - 1][i] == 'M' && puzzle[j - 2][i] == 'A' && puzzle[j - 3][i] == 'S') acc1++
                    if (i > 2 && puzzle[j - 1][i - 1] == 'M' && puzzle[j - 2][i - 2] == 'A' && puzzle[j - 3][i - 3] == 'S') acc1++
                    if (i < size - 3 && puzzle[j - 1][i + 1] == 'M' && puzzle[j - 2][i + 2] == 'A' && puzzle[j - 3][i + 3] == 'S') acc1++
                }
                if (i > 2 && puzzle[j][i - 1] == 'M' && puzzle[j][i - 2] == 'A' && puzzle[j][i - 3] == 'S') {
                    acc1++
                }
                if (i < size - 3 && puzzle[j][i + 1] == 'M' && puzzle[j][i + 2] == 'A' && puzzle[j][i + 3] == 'S') {
                    acc1++
                }
                if (j < size - 3) {
                    if (puzzle[j + 1][i] == 'M' && puzzle[j + 2][i] == 'A' && puzzle[j + 3][i] == 'S') acc1++
                    if (i > 2 && puzzle[j + 1][i - 1] == 'M' && puzzle[j + 2][i - 2] == 'A' && puzzle[j + 3][i - 3] == 'S') acc1++
                    if (i < size - 3 && puzzle[j + 1][i + 1] == 'M' && puzzle[j + 2][i + 2] == 'A' && puzzle[j + 3][i + 3] == 'S') acc1++
                }
            }
        }

        // part 2
        for (i in 1..<size-1) {
            for (j in 1..<size-1) {
                if (puzzle[j][i] != 'A') {
                    continue
                }

                if ((puzzle[j-1][i-1] == 'M' && puzzle[j+1][i+1] == 'S') ||
                    (puzzle[j+1][i+1] == 'M' && puzzle[j-1][i-1] == 'S')) {
                    if ((puzzle[j-1][i+1] == 'M' && puzzle[j+1][i-1] == 'S') ||
                        (puzzle[j+1][i-1] == 'M' && puzzle[j-1][i+1] == 'S')) {
                        acc2++
                    } else {
                        continue
                    }
                } else if ((puzzle[j-1][i+1] == 'M' && puzzle[j+1][i-1] == 'S') ||
                    (puzzle[j+1][i-1] == 'M' && puzzle[j-1][i+1] == 'S')) {
                    if ((puzzle[j-1][i-1] == 'M' && puzzle[j+1][i+1] == 'S') ||
                        (puzzle[j+1][i+1] == 'M' && puzzle[j-1][i-1] == 'S')) {
                        acc2++
                    } else {
                        continue
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
