fun main() {
    fun part1(input: List<String>): Int {
        return input.asSequence().map {
            val iter = it.asSequence().filter { it.isDigit() }
            val first = iter.first()
            val last = iter.last()
            "$first$last".toInt()
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val nbrSpelled = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        val reg = Regex("^(one|two|three|four|five|six|seven|eight|nine|[1-9]).*$")

        fun transform(s: String) = if (s.length == 1) {
            s
        } else {
            "${nbrSpelled.indexOf(s) + 1}"
        }

        return input.asSequence().map { i ->
            val a = i.indices.mapNotNull {
                reg.find(i.substring(it))?.groupValues?.get(1)
            }
            transform(a.first()) + transform(a.last())
        }.map { it.toInt() }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()

    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    part2(input).println()
}
