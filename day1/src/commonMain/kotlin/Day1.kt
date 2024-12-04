@file:OptIn(ExperimentalForeignApi::class)

import kotlinx.cinterop.*
import platform.posix.*

fun main() {
    val lists = read()
    val firstList = lists.first.sorted()
    val secondList = lists.second.sorted()
    var acc = 0
    for (i in firstList.indices) {
        acc += abs(firstList[i] - secondList[i])
    }
    println(acc)
}

fun read(): Pair<List<Int>, List<Int>> {
    return input
}

private val input: Pair<List<Int>, List<Int>> by lazy { readInput() }

@OptIn(ExperimentalForeignApi::class)
private fun readInput(): Pair<List<Int>, List<Int>> {
    val cwd = getcwd(null, 0U)?.toKString()
    val resourceDir = "${cwd}/src/commonMain/resources"
    val file = fopen("$resourceDir/Day1.input", "r") ?:
    throw IllegalArgumentException("Cannot open input file Day1.input")

    val firstList = ArrayList<Int>()
    val secondList = ArrayList<Int>()

    try {
        memScoped {
            val readBufferLength = 64 * 1024
            val buffer = allocArray<ByteVar>(readBufferLength)
            var line = fgets(buffer, readBufferLength, file)?.toKString()
            var values: List<String>
            val delimiter = "   "
            while (line != null) {
                values = line?.split(delimiter, limit = 2)!!
                firstList.add(values.first().toInt())
                secondList.add(values.last().trim().toInt())

                line = fgets(buffer, readBufferLength, file)?.toKString()
            }
        }
    } finally {
        fclose(file)
    }

    return Pair(firstList, secondList)
}
