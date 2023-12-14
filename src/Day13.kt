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

    fun canColumnBeFixed(pattern: List<List<Boolean>>, columnOne: Int, columnTwo: Int): Boolean {
        var diff = 0
        for (i in pattern.indices) {
            if (pattern[i][columnOne] != pattern[i][columnTwo]) {
                diff += 1
            }
        }

        return diff == 1
    }

    fun canRowBeFixed(lineOne: List<Boolean>, lineTwo: List<Boolean>): Boolean {
        var diff = 0
        for (i in lineOne.indices) {
            if (lineOne[i] != lineTwo[i]) {
                diff += 1
            }
        }

        return diff == 1
    }

    fun fixRow(pattern : List<List<Boolean>>) : Long{
        a@ for (l in 1 until pattern.size) {
            var fixed = false
            var i = 0

            while (l - i - 1 >= 0 && l + i < pattern.size) {
                if (pattern[l - i - 1] != pattern[l + i]) {
                    if (canRowBeFixed(pattern[l - i - 1], pattern[l + i]) && !fixed) {
                        fixed = true
                    } else {
                        continue@a
                    }
                }
                i += 1
            }

            if (fixed) {
                return l * 100L
            }
        }

        return 0L
    }

    fun fixColumn(pattern : List<List<Boolean>>):Long {
        a@ for (c in 1 until pattern[0].size) {
            var fixed = false
            var i = 0

            while (c - i - 1 >= 0 && c + i < pattern[0].size) {
                if (!columnSimilar(pattern, c - i -1, c + i)) {
                    if (canColumnBeFixed(pattern, c - i - 1, c + i) && !fixed) {
                        fixed = true
                    } else {
                        continue@a
                    }
                }
                i += 1
            }

            if (fixed) {
                return c.toLong()
            }
        }

        return 0L
    }

    fun part2(input: List<String>): Long {
        return part(input) { pattern ->
            fixRow(pattern) + fixColumn(pattern)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405L)

    val input = readInput("Day13")
    part1(input).println()

    check(part2(testInput) == 400L)


    check(part2(input) < 48400L) { "inf" }
    check(part2(input) > 42857L) { "sup" }
    part2(input).println()
}
