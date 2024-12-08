@file:OptIn(ExperimentalForeignApi::class)

import kotlinx.cinterop.*
import platform.posix.*

fun main() {
    read()
}

fun read(): Any {
    return input
}

private val input: String by lazy { readInput() }

fun removeOneElement(report: List<Int>, ind: Int): List<Int> {
    return report.subList(0, ind) + report.subList(ind + 1, report.size)
}

fun isGood(report: List<Int>): Boolean {
    for (i in 0..report.size - 2) {
        if (report[i] - report[i+1] !in 1..3) {
            return false
        }
    }
    return true
}

fun isGoodWithCorrection(report: List<Int>): Boolean {
    for (i in 0..report.size - 2) {
        if (report[i] - report[i+1] !in 1..3) {
            return isGood(removeOneElement(report, i)) || isGood(removeOneElement(report, i+1))
        }
    }
    return true
}

@OptIn(ExperimentalForeignApi::class)
private fun readInput(): String {
    val cwd = getcwd(null, 0U)?.toKString()
    val resourceDir = "${cwd}/src/commonMain/resources"
    val file = fopen("$resourceDir/Day2.input", "r") ?:
    throw IllegalArgumentException("Cannot open input file Day2.input")

    try {
        memScoped {
            val readBufferLength = 64 * 1024
            val buffer = allocArray<ByteVar>(readBufferLength)
            var line = fgets(buffer, readBufferLength, file)?.toKString()
            var report: List<Int>
            var delimiter = " "
            var safeReportCounter = 0

            while (line != null) {
                report = line?.trim()?.split(delimiter)?.map { it.toInt() }!!
//                println(report)

                if (isGoodWithCorrection(report) || isGoodWithCorrection(report.reversed())) {
                    safeReportCounter++
                }

                line = fgets(buffer, readBufferLength, file)?.toKString()
            }

            println(safeReportCounter)
        }
    } finally {
        fclose(file)
    }

    return ""
}
