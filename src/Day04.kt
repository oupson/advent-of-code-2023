data class Card(
    val cardNumber: Int, val winningNumbers: List<Int>, val numberPicked: List<Int>
)

fun Sequence<String>.toCardSequence() = this.map { line ->
    line.replace("Card\\s*".toRegex(), "").split(": ").let { splitCard ->
        val cardNbr = splitCard[0].toInt()
        val split = splitCard[1].split(
            " | "
        )
        Card(cardNbr,
            split[0].split(" ").filter { it.isNotEmpty() }.map { it.toInt() },
            split[1].split(" ").filter { it.isNotEmpty() }.map { it.toInt() })
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.asSequence().toCardSequence().map { c ->
            var res = 0
            for (winning in c.winningNumbers) {
                if (winning in c.numberPicked) {
                    if (res == 0) {
                        res = 1
                    } else {
                        res *= 2
                    }
                }
            }

            res
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.asSequence().toCardSequence().map { c ->
            var nbr = 0
            for (winning in c.winningNumbers) {
                if (winning in c.numberPicked) {
                    nbr += 1
                }
            }

            nbr
        }.fold(Pair(mutableListOf(1), 0)) { acc, ints ->
            val count = acc.first.removeAt(0)

            val newList = if (acc.first.isEmpty()) {
                mutableListOf(1)
            } else {
                acc.first.also {
                    it[0] += 1
                }
            }


            for (i in 0 until ints) {
                if (newList.size > i) {
                    newList[i] += count
                } else {
                    newList.add(count)
                }
            }

            Pair(
                newList, acc.second + count
            )
        }.second
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()

    check(part2(testInput) == 30)

    part2(input).println()
}
