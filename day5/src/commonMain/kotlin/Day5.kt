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

fun putOrAddtoMap(key: Int, value: Int, map: HashMap<Int, HashSet<Int>>) {
    if (map.containsKey(key)) {
        map[key]?.add(value)
    } else {
        map[key] = HashSet()
        map[key]?.add(value)
    }
}

fun isGood(pages: List<Int>, beforeMap: HashMap<Int, HashSet<Int>>): Boolean {
    for (i in 1..<pages.size) {
        if (beforeMap.containsKey(pages[i]) && beforeMap[pages[i]]?.containsAll(pages.subList(0, i))!!) {
            // report is good so far
        } else {
            return false
        }
    }
    return true
}

class RulesComparator(val beforeMap: HashMap<Int, HashSet<Int>>, val afterMap: HashMap<Int, HashSet<Int>>): Comparator<Int> {
    override fun compare(p1: Int, p2: Int): Int {
        if (this.beforeMap.containsKey(p1) && this.beforeMap[p1]?.contains(p2)!!) return 1
        if (this.afterMap.containsKey(p2) && this.afterMap[p2]?.contains(p1)!!) return -1
        return 0
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun readInput(): String {
    val cwd = getcwd(null, 0U)?.toKString()
    val resourceDir = "${cwd}/src/commonMain/resources"
    val file = fopen("$resourceDir/Day5.input", "r") ?:
    throw IllegalArgumentException("Cannot open input file Day5.input")

    val beforeMap = HashMap<Int, HashSet<Int>>()
    val afterMap = HashMap<Int, HashSet<Int>>()
    var readingReports = true
    var pages: List<Int>
    var sortedPages: List<Int>
    var acc = 0
    var beforeVal: Int
    var afterVal: Int
    var rulesComparator = RulesComparator(HashMap(), HashMap())

    try {
        memScoped {
            val readBufferLength = 64 * 1024
            val buffer = allocArray<ByteVar>(readBufferLength)
            var line = fgets(buffer, readBufferLength, file)?.toKString()
            while (line != null) {
                // operate on line
                if (line.trim() == "") {
                    //println("before map: " + beforeMap)
                    //println("before map with counts: " + beforeMap.map { Pair(it.key, it.value.size) }.toList())
                    rulesComparator = RulesComparator(beforeMap, afterMap)
                    readingReports = false
                    line = fgets(buffer, readBufferLength, file)?.toKString()
                    continue
                }

                if (readingReports) {
                    beforeVal = line.substring(0, 2).toInt()
                    afterVal = line.substring(3, 5).toInt()
                    putOrAddtoMap(afterVal, beforeVal, beforeMap)
                    putOrAddtoMap(beforeVal, afterVal, afterMap)
                }
                if (!readingReports) {
                    pages = line.trim().split(',').map { it.toInt() }.toList()
                    if (!isGood(pages, beforeMap)) {
                        //println(pages)
                        sortedPages = pages.sortedWith(rulesComparator)
                        //println(sortedPages)
                        acc += sortedPages[sortedPages.size / 2]
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
