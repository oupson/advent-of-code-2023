fun main() {
    fun distance(g1: Pair<Int, Int>, g2: Pair<Int, Int>): Int {
        var currentX = g1.second
        var currentY = g1.first

        var step = 0

        while (currentX != g2.second) {
            currentX += if (g2.second > currentX) {
                1
            } else {
                -1
            }

            step += 1
        }

        while (currentY != g2.first) {
            currentY += if (g2.first > currentY) {
                1
            } else {
                -1
            }

            step += 1
        }

        return step
    }

    fun part1(input: List<String>): Int {
        val galaxy = input.asSequence().map {
            it.map { s -> s == '#' }.toMutableList()
        }.toMutableList()

        var index = 0
        while (index < galaxy.size) {
            if (galaxy[index].all { !it }) {
                galaxy.add(index + 1, MutableList(galaxy[0].size) { false })
                println("expanding row")
                index += 2
            } else {
                index += 1
            }
        }

        index = 0
        var size = galaxy[0].size
        while (index < size) {
            val colEmpty = (0 until galaxy.size).map {
                galaxy[it][index]
            }.all { !it }

            if (colEmpty) {
                galaxy.forEach { it.add(index, false) }
                println("expanding col")
                size += 1
                index += 2
            } else {
                index += 1
            }
        }

        val galaxies = galaxy.asSequence().flatMapIndexed { row: Int, booleans: MutableList<Boolean> ->
            booleans.asSequence().mapIndexedNotNull { col, isGalaxy -> if (isGalaxy) Pair(row, col) else null }
        }.toList()

        var res = 0
        for (galaxyIndex in galaxies.indices) {
            val g1 = galaxies[galaxyIndex]

            for (i in (galaxyIndex + 1) until galaxies.size) {
                val g2 = galaxies[i]
                res += distance(g1, g2)
            }

        }

        return res
    }

    fun part2(input: List<String>, factor: Int = 1000000): Long {
        val galaxy = input.asSequence().map {
            it.map { s -> s == '#' }.toMutableList()
        }.toMutableList()

        val cols = List(galaxy[0].size) { x ->
            if (galaxy.all {
                    !it[x]
                }) {
                factor
            } else {
                1
            }
        }

        val lines = List(galaxy.size) { y ->
            if (galaxy[y].all { !it }) {
                factor
            } else {
                1
            }
        }

        val galaxies = galaxy.asSequence().flatMapIndexed { row: Int, booleans: MutableList<Boolean> ->
            booleans.asSequence().mapIndexedNotNull { col, isGalaxy -> if (isGalaxy) Pair(row, col) else null }
        }.toList()

        var res = 0L
        for (index in galaxies.indices) {
            val g1 = galaxies[index]

            for (i in (index + 1) until galaxies.size) {
                val g2 = galaxies[i]
                res += run {
                    var currentX = g1.second
                    var currentY = g1.first

                    var step = 0

                    while (currentX != g2.second) {
                        currentX += if (g2.second > currentX) {
                            1
                        } else {
                            -1
                        }

                        step += cols[currentX]
                    }

                    while (currentY != g2.first) {
                        currentY += if (g2.first > currentY) {
                            1
                        } else {
                            -1
                        }

                        step += lines[currentY]
                    }

                    step
                }
            }

        }

        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374)

    val input = readInput("Day11")
    part1(input).println()

    check(part2(testInput, 10) == 1030L)
    check(part2(testInput, 100) == 8410L)

    part2(input).println()
}
