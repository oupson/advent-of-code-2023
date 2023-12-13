fun main() {
    fun printInput(input: List<List<Boolean>>) {
        for (l in input) {
            println(l.map {
                if (it) {
                    '#'
                } else {
                    "."
                }
            }.joinToString(""))
        }
        println()
    }

    fun findRow(input: List<List<Boolean>>): Long {
        a@ for (r in 1 until input.size) {
            if (input[r - 1] == input[r]) {
                var i = 1
                while (r - i - 1 >= 0 && r + i < input.size) {
                    if (input[r - i - 1] != input[r + i]) {
                        continue@a
                    }
                    i += 1
                }

                return r * 100L
            }
        }
        return 0L
    }

    fun columnSimilar(input: List<List<Boolean>>, columnIndex: Int, columnIndex2: Int, d: Boolean = false): Boolean {
        for (i in input.indices) {
            if (input[i][columnIndex] != input[i][columnIndex2]) {
                return false
            }
        }

        return true
    }

    fun findColumn(input: List<List<Boolean>>): Long {
        var res = 0L
        a@ for (c in 1 until input[0].size) {
            if (columnSimilar(input, c - 1, c)) {
                var i = 0
                while (c - i - 1 >= 0 && c + i < input[0].size) {
                    if (!columnSimilar(input, c - i - 1, c + i, true)) {
                        continue@a
                    }
                    i += 1
                }

                res = c.toLong()
            }
        }

        return res
    }


    fun part(input: List<String>, processor: (List<List<Boolean>>) -> Long): Long {
        var res = 0L
        val currentPattern = mutableListOf<List<Boolean>>()
        for (line in input) {
            if (line.isEmpty()) {
                res += processor.invoke(currentPattern)
                currentPattern.clear()
            } else {
                currentPattern.add(line.map { it == '#' })
            }
        }

        res += processor.invoke(currentPattern)

        return res
    }


    fun part1(input: List<String>): Long {
        return part(input) { pattern -> findColumn(pattern) + findRow(pattern) }
    }

    fun canBeFixed(lineOne: List<Boolean>, lineTwo: List<Boolean>): Boolean {
        var diff = 0
        for (i in lineOne.indices) {
            if (lineOne[i] != lineTwo[i]) {
                diff += 1
            }
        }

        return diff == 1
    }

    fun part2(input: List<String>): Long {
        return part(input) { pattern ->
            a@ for (l in 1 until pattern.size) {
                var fixed = false
                var i = 0

                while (l - i - 1 >= 0 && l + i < pattern.size) {
                    if (pattern[l - i - 1] != pattern[l + i]) {
                        if (canBeFixed(pattern[l - i - 1], pattern[l + i]) && !fixed) {
                            fixed = true
                        } else {
                            continue@a
                        }
                    }
                    i += 1
                }

                if (fixed) {
                    return@part l * 100L
                }
            }


            printInput(pattern)
            0L
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405L)

    val input = readInput("Day13")
    part1(input).println()

    check(part2(testInput) == 400L)

    part2(input).println()
}
