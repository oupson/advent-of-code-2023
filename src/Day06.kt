fun main() {
    fun part1(input: List<String>): Int {
        val times = input[0].removePrefix("Time:").trimStart().split("\\s+".toRegex()).map { it.toInt() }
        val distances = input[1].removePrefix("Distance:").trimStart().split("\\s+".toRegex()).map { it.toInt() }


        return times.zip(distances).map {
            var res = 0
            for (t in 1 until it.first) {
                if (t * (it.first - t) > it.second) {
                    res += 1
                }
            }

            res
        }.fold(1) { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Int {
        val time = input[0].removePrefix("Time:").trimStart().replace("\\s+".toRegex(), "").toLong()
        val distance = input[1].removePrefix("Distance:").trimStart().replace("\\s".toRegex(), "").toLong()

        var res = 0
        for (t in 1 until time) {
            if (t * (time - t) > distance) {
                res += 1
            }
        }

        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)

    val input = readInput("Day06")
    part1(input).println()

    check(part2(testInput) == 71503)

    part2(input).println()
}
