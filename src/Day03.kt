import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

sealed class PartValue(val char: Char) {
    class PartNumber(char: Char, var isValid: Boolean) : PartValue(char)
    data object Point : PartValue('.')
    class Symbol(char: Char) : PartValue(char)
}

sealed class PartValue2(val location: Pair<Int, IntRange>) {
    class Number(location: Pair<Int, IntRange>, val nbr: Int) : PartValue2(location)
    class Gear(location: Pair<Int, IntRange>) : PartValue2(location)
}

fun main() {
    fun part1(input: List<String>): Int {
        val matrix = input.asSequence().map {
            it.toCharArray().map { c ->
                when {
                    c.isDigit() -> PartValue.PartNumber(c, false)
                    c == '.' -> PartValue.Point
                    else -> PartValue.Symbol(c)
                }
            }
        }.toList()

        for (line in matrix.indices) {
            for (col in matrix[line].indices) {
                val partValue = matrix[line][col]

                if (partValue is PartValue.PartNumber) {
                    var isValid = false

                    loop@ for (checkLine in max(line - 1, 0) until min(line + 2, matrix.size)) {
                        for (checkCol in max(col - 1, 0) until min(col + 2, matrix[line].size)) {
                            val s = matrix[checkLine][checkCol]
                            if (s is PartValue.Symbol) {
                                isValid = true
                                break@loop
                            }
                        }
                    }

                    partValue.isValid = isValid
                }
            }
        }

        return matrix.asSequence().map {
            var value = 0
            var lastNumber: Pair<String, Boolean>? = null

            for (symbol in it) {
                if (symbol is PartValue.PartNumber) {
                    lastNumber = if (lastNumber == null) {
                        Pair(symbol.char.toString(), symbol.isValid)
                    } else {
                        Pair(lastNumber.first + symbol.char, lastNumber.second || symbol.isValid)
                    }
                } else if (lastNumber != null) {
                    if (lastNumber.second) {
                        value += lastNumber.first.toInt()
                    }
                    lastNumber = null
                }
            }

            if (lastNumber != null && lastNumber.second) {
                value += lastNumber.first.toInt()
            }

            value
        }.sum()

    }

    fun part2(input: List<String>): Int {
        val regex = Regex("(\\d+)|([^.])")
        val parts = input.asSequence().flatMapIndexed { line, lineValue ->
            regex.findAll(lineValue).map { c ->
                if (c.groups[1] != null) {
                    PartValue2.Number(Pair(line, c.groups[1]!!.range), c.groups[1]!!.value.toInt())
                } else {
                    PartValue2.Gear(Pair(line, c.groups[2]!!.range))
                }
            }
        }.toList()

        return parts.asSequence().filter { it is PartValue2.Gear }.map { gear ->
                val gearX = gear.location.second.first
                parts.filter { part2 ->
                    part2 is PartValue2.Number && abs(gear.location.first - part2.location.first) <= 1 && part2.location.second.let {
                        gearX >= it.first - 1 && gearX <= it.last + 1
                    }
                }
            }.filter { it.size == 2 }.map {
                it.fold(1) { acc, p -> (p as PartValue2.Number).nbr * acc }
            }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()

    check(part2(testInput) == 467835)

    part2(input).println()
}
