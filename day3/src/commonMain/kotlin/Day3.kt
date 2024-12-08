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

enum class MatchType {
    MUL, DO, DONT
}

class MatchContainer(val type: MatchType, val ind: Int, val val1: Int = 0, val val2: Int = 0): Comparable<MatchContainer> {
    override fun compareTo(other: MatchContainer): Int {
        return this.ind compareTo other.ind
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun readInput(): String {
    val cwd = getcwd(null, 0U)?.toKString()
    val resourceDir = "${cwd}/src/commonMain/resources"
    val file = fopen("$resourceDir/Day3.input", "r") ?:
    throw IllegalArgumentException("Cannot open input file Day3.input")

    try {
        memScoped {
            val readBufferLength = 64 * 1024
            val buffer = allocArray<ByteVar>(readBufferLength)

            val regexMul = Regex("mul\\((\\d{1}|\\d{2}|\\d{3}),(\\d{1}|\\d{2}|\\d{3})\\)")
            val regexDo = Regex("do\\(\\)")
            val regexDont = Regex("don't\\(\\)")

            var matches: Sequence<MatchResult>
            var doList: List<Int>
            var dontList: List<Int>

            var allMatches: List<MatchContainer>
            var acc = 0
            var mulEnabled = true

            var line = fgets(buffer, readBufferLength, file)?.toKString()
            while (line != null) {
                allMatches = regexMul.findAll(line).map { MatchContainer(MatchType.MUL, it.range.first, it.groupValues[1].toInt(), it.groupValues[2].toInt()) }.toList()
                allMatches += regexDo.findAll(line).map { MatchContainer(MatchType.DO, it.range.first) }.toList()
                allMatches += regexDont.findAll(line).map { MatchContainer(MatchType.DONT, it.range.first) }.toList()
                allMatches = allMatches.sorted()

                for (match in allMatches) {
                    //println(match.type)
                    //println(match.range.first)
                    if (match.type == MatchType.DO) {
                        mulEnabled = true
                    } else if (match.type == MatchType.DONT) {
                        mulEnabled = false
                    } else if (match.type == MatchType.MUL && mulEnabled) {
                        acc += match.val1 * match.val2
                    }
                }

                line = fgets(buffer, readBufferLength, file)?.toKString()
            }

            println(acc)
        }
    } finally {
        fclose(file)
    }

    return ""
}
