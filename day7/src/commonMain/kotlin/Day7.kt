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

fun testEquation(expected: Long, values: List<Long>, index: Int): Boolean {
    if (index == 0) {
        return expected == values[index]
    }
    if (expected <= 0) {
        return false
    }
    if (expected % values[index] == 0L) {
        if (testEquation(expected / values[index], values, index - 1)) {
            return true
        }
    }
    if (expected.toString().endsWith(values[index].toString())) {
        val suffix = values[index].toString().length
        val powerOf10 = pow(10.0, suffix.toDouble()).toLong()
        if (testEquation(expected / powerOf10, values, index - 1)) {
            return true
        }
    }
    return testEquation(expected - values[index], values, index - 1)
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

            if (testEquation(expected, values, values.size - 1)) {
                acc += expected
            }
        }

        println(acc)
    } finally {
        fclose(file)
    }

    return ""
}
