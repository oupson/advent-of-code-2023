import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        val segments = input.asSequence().map { line ->
            val (dir, size) = line.split(' ')

            Pair(dir, size.toInt())
        }.fold(
            Pair(
                Pair(0, 0), mutableListOf(Pair(0, 0))
            )
        ) { (lastPos, segments), (direction, size) ->

            val newPoint = when (direction) {
                "R" -> Pair(lastPos.first, lastPos.second + size)
                "L" -> Pair(lastPos.first, lastPos.second - size)
                "D" -> Pair(lastPos.first + size, lastPos.second)
                else -> Pair(lastPos.first - size, lastPos.second)
            }

            segments.add(newPoint)
            Pair(newPoint, segments)
        }.second.windowed(2).map { (first, second) ->
            if (first.first == second.first) {
                Triple(false, first.first, min(first.second, second.second)..max(first.second, second.second))
            } else {
                Triple(true, first.second, min(first.first, second.first)..max(first.first, second.first))
            }
        }

        val toFill = mutableListOf(Pair(1, 1))
        val filled = mutableListOf<Pair<Int, Int>>()

        while (toFill.isNotEmpty()) {
            val f = toFill.removeAt(0)
            filled.add(f)

            val (y, x) = f
            if (!segments.any { it.first && it.second == x - 1 && y in it.third }) {
                val p = Pair(y, x - 1)
                if (!toFill.contains(p) && !filled.contains(p)) {
                    toFill.add(p)
                }
            }

            if (!segments.any { it.first && it.second == x + 1 && y in it.third }) {
                val p = Pair(y, x + 1)
                if (!toFill.contains(p) && !filled.contains(p)) {
                    toFill.add(p)
                }
            }

            if (!segments.any { !it.first && it.second == y - 1 && x in it.third }) {
                val p = Pair(y - 1, x)
                if (!toFill.contains(p) && !filled.contains(p)) {
                    toFill.add(p)
                }
            }

            if (!segments.any { !it.first && it.second == y + 1 && x in it.third }) {
                val p = Pair(y + 1, x)
                if (!toFill.contains(p) && !filled.contains(p)) {
                    toFill.add(p)
                }
            }
        }

        return filled.size + segments.sumOf { it.third.last - it.third.first }
    }

    fun part2(input: List<String>): Long {
        val segments = input.asSequence().map { line ->
            val (_, _, encoded) = line.split(' ')

            val size = encoded.slice(2..6).toInt(16)
            Pair(
                when (encoded[7]) {
                    '0' -> 'R'
                    '1' -> 'D'
                    '2' -> 'L'
                    else -> 'U'
                }, size
            )
        }.fold(
            Pair(
                Pair(0, 0), mutableListOf(Pair(0, 0))
            )
        ) { (lastPos, segments), (direction, size) ->

            val newPoint = when (direction) {
                'R' -> Pair(lastPos.first, lastPos.second + size)
                'L' -> Pair(lastPos.first, lastPos.second - size)
                'D' -> Pair(lastPos.first + size, lastPos.second)
                else -> Pair(lastPos.first - size, lastPos.second)
            }

            segments.add(newPoint)
            Pair(newPoint, segments)
        }.second.windowed(2).map { (first, second) ->
            if (first.first == second.first) {
                Triple(false, first.first, min(first.second, second.second)..max(first.second, second.second))
            } else {
                Triple(true, first.second, min(first.first, second.first)..max(first.first, second.first))
            }
        }

        val minY = segments.filter { !it.first }.minOf { it.second }
        val maxY = segments.filter { !it.first }.maxOf { it.second }

        var res = 0L
        for (y in minY..maxY) {
            val xs = segments.filter { it.first && y in it.third }.sortedBy { it.second }

            for (x in xs.windowed(2)) {
                res += abs(x.maxOf { it.second } - x.minOf { it.second }) + 1
            }
        }

        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 62)

    val input = readInput("Day18")
    part1(input).println()

    check(part2(testInput) == 952408144115L)

    part2(input).println()
}
