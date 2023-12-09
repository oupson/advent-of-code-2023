suspend fun SequenceScope<List<Int>>.processLine(lastLine: List<Int>) {
    if (lastLine.any { it != 0 }) {
        val newLine = lastLine.windowed(2).map {
            it[1] - it[0]
        }
        this.processLine(newLine)
    }
    this.yield(lastLine)
}

fun lineSequence(line: List<Int>) = sequence {
    this.processLine(line)
}

fun processLine(line: List<Int>): Int {
    val lineSequence = lineSequence(line)

    return lineSequence.fold(0) { acc, ints ->
        acc + ints.last()
    }
}

fun processLine2(line: List<Int>): Int {
    val lineSequence = lineSequence(line)

    return lineSequence.fold(0) { acc, ints ->
        ints.first() - acc
    }
}


fun main() {
    fun part1(input: List<String>): Int {
        return input.asSequence().map {
            it.split(" ")
        }.map {
            it.map { nbr -> nbr.toInt() }
        }.map {
            processLine(it)
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.asSequence().map {
            it.split(" ")
        }.map {
            it.map { nbr -> nbr.toInt() }
        }.map {
            processLine2(it)
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)

    val input = readInput("Day09")
    part1(input).println()

    check(part2(testInput) == 2)

    part2(input).println()
}
