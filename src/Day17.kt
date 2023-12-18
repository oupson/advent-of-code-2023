fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.map { line -> line.map { it.digitToInt() } }

        val possibilities = mutableListOf<Pair<List<Pair<Int, Int>>, Int>>()
        var minPossibility: Int? = null

        while (possibilities.isNotEmpty()) {
            
        }

        return minPossibility!!
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 0)

    val input = readInput("Day17")
    part1(input).println()

    check(part2(testInput) == 0)

    part2(input).println()
}
