import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        return input.asSequence().map {
            it.split(": ").let {
                Pair(it.first().removePrefix("Game ").toInt(), it.last().split("; ").map {
                    val setValue = mutableMapOf<String, Int>()
                    it.split(", ").map {
                        val s = it.split(" ")
                        setValue[s.last()] = (setValue[s.last()] ?: 0) + s.first().toInt()
                    }

                    setValue
                })
            }
        }.filter {
            it.second.none { set ->
                set.getOrDefault("red", 0) > 12 || set.getOrDefault("green", 0) > 13 || set.getOrDefault("blue", 0) > 14
            }
        }.map { it.first }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.asSequence().map {
            it.split(": ").let {
                Pair(it.first().removePrefix("Game ").toInt(), it.last().split("; ").map {
                    val setValue = mutableMapOf<String, Int>()
                    it.split(", ").map {
                        val s = it.split(" ")
                        setValue[s.last()] = (setValue[s.last()] ?: 0) + s.first().toInt()
                    }

                    setValue
                })
            }
        }.map {
            it.second.fold(mutableMapOf<String, Int>()) { acc, set ->
                for (comp in set) {
                    acc[comp.key] = max(acc[comp.key] ?: 0, comp.value)
                }
                acc
            }
        }.map { it.values.fold(1) { acc, i -> acc * i } }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()

    check(part2(testInput) == 2286)

    part2(input).println()
}
