fun main() {
    fun parseInput(input: Sequence<String>): Platform {
        return input.map {
            it.toCharArray()
        }.toList()
    }

    fun slideVertical(platform: Platform, north: Boolean) {
        val width = platform[0].size
        val dir = if (north) -1 else 1

        val lineIter = if (north) {
            platform.indices
        } else {
            platform.indices.reversed()
        }

        for (lineIndex in lineIter) {
            for (columnIndex in 0 until width) {
                if (platform[lineIndex][columnIndex] == 'O') {
                    var newLine = lineIndex
                    while (newLine + dir in platform.indices && platform[newLine + dir][columnIndex] == '.') {
                        newLine += dir
                    }

                    platform[lineIndex][columnIndex] = '.'
                    platform[newLine][columnIndex] = 'O'
                }
            }
        }
    }

    fun slideHorizontal(platform: Platform, east: Boolean) {
        val width = platform[0].size
        val dir = if (east) 1 else -1

        val colIter = if (east) {
            (0 until width).reversed()
        } else {
            (0 until width)
        }

        for (lineIndex in platform.indices) {
            for (columnIndex in colIter) {
                if (platform[lineIndex][columnIndex] == 'O') {
                    var newCol = columnIndex
                    while (newCol + dir in 0 until width && platform[lineIndex][newCol + dir] == '.') {
                        newCol += dir
                    }

                    platform[lineIndex][columnIndex] = '.'
                    platform[lineIndex][newCol] = 'O'
                }
            }
        }
    }

    fun part1(input: List<String>): Long {
        val platform = parseInput(
            input.asSequence()
        )

        slideVertical(platform, true)

        return platform.mapIndexed { i, it ->
            it.fold(0L) { acc, c ->
                acc + if (c == 'O') {
                    (platform.size - i).toLong()
                } else {
                    0L
                }
            }
        }.sum()
    }

    fun part2(input: List<String>): Long {
        val platform = parseInput(
            input.asSequence()
        )

        for (i in 0 until 1000L) {
            slideVertical(platform, true)
            slideHorizontal(platform, false)
            slideVertical(platform, false)
            slideHorizontal(platform, true)
        }


        return platform.mapIndexed { i, it ->
            it.fold(0L) { acc, c ->
                acc + if (c == 'O') {
                    (platform.size - i).toLong()
                } else {
                    0L
                }
            }
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136L)

    val input = readInput("Day14")
    part1(input).println()

    check(part2(testInput) == 64L)

    part2(input).println()
}

typealias Platform = List<CharArray>
