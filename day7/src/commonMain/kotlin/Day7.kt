@file:OptIn(ExperimentalForeignApi::class)

import kotlinx.cinterop.*
import platform.posix.*

fun main() {
    println(read())
}

fun read(): Any {
    return input.reversed()
}

private val input: String by lazy { readInput() }

fun concatenate(val1: Long, val2: Long): Long {
    var xScale = 1
    while (xScale <= val2) {
        xScale *= 10
    }
    return val1 * xScale + val2
}

fun testEquation(expected: Long, values: List<Long>, current: Long, index: Int): Boolean {
    if (index == 0) {
        return testEquation(expected, values, values[0], 1)
    }
    if (index == values.size - 1) {
        return (expected == current+values[index]) ||
                (expected == current*values[index]) ||
                (expected == concatenate(current, values[index]))
    }
    return testEquation(expected, values, current+values[index], index+1) ||
            testEquation(expected, values, current*values[index], index+1) ||
            testEquation(expected, values, concatenate(current, values[index]), index+1)
}

@OptIn(ExperimentalForeignApi::class)
private fun readInput(): String {
    val cwd = getcwd(null, 0U)?.toKString()
    val resourceDir = "${cwd}/src/commonMain/resources"
    val file = fopen("$resourceDir/Day7.input", "r") ?:
    throw IllegalArgumentException("Cannot open input file Day7.input")

    val calibrations = ArrayList<String>()

    try {
        memScoped {
            val readBufferLength = 64 * 1024
            val buffer = allocArray<ByteVar>(readBufferLength)
            var line = fgets(buffer, readBufferLength, file)?.toKString()
            while (line != null) {
                // todo
                calibrations.add(line.trim())
                line = fgets(buffer, readBufferLength, file)?.toKString()
            }
        }

        var expected: Long
        var values: List<Long>
        var splitResult: List<String>
        var acc: Long = 0

        for (calibration in calibrations) {
            splitResult = calibration.split(":", limit = 2)
            expected = splitResult[0].toLong()
            values = splitResult[1].trim().split(" ").map { it.toLong() }.toList()

            if (testEquation(expected, values, 0, 0)) {
                acc += expected
            }
        }

        println(acc)
    } finally {
        fclose(file)
    }

    return ""
}
