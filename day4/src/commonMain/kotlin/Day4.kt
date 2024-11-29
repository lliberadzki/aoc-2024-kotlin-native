@file:OptIn(ExperimentalForeignApi::class)

import kotlinx.cinterop.*
import platform.posix.*

fun main() {
    println(part1())
    println(part2())
}

fun part1(): Any {
    return input
}

fun part2(): Any {
    return input.reversed()
}

private val input: String by lazy { readInput()}

private fun readInput(): String {
    val returnBuffer = StringBuilder()
    val cwd = getcwd(null, 0U)?.toKString()
    val resourceDir = "${cwd}/src/commonMain/resources"
    val file = fopen("$resourceDir/Day4.input", "r") ?:
    throw IllegalArgumentException("Cannot open input file Day4.input")

    try {
        memScoped {
            val readBufferLength = 64 * 1024
            val buffer = allocArray<ByteVar>(readBufferLength)
            var line = fgets(buffer, readBufferLength, file)?.toKString()
            while (line != null) {
                returnBuffer.append(line)
                line = fgets(buffer, readBufferLength, file)?.toKString()
            }
        }
    } finally {
        fclose(file)
    }

    return returnBuffer.toString()
}
