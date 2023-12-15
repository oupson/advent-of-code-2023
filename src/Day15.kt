fun main() {
    class Box(val lensList: MutableList<Pair<String, Int>> = mutableListOf()) {
        fun append(label: String, lensValue: Int) {
            val index = this.lensList.indexOfFirst { it.first == label }
            if (index < 0) {
                this.lensList.add(Pair(label, lensValue))
            } else {
                this.lensList[index] = Pair(label, lensValue)
            }
        }

        fun removeIfExist(label: String) {
            this.lensList.removeIf { it.first == label }
        }
    }

    fun hash(input: String): Long {
        var value = 0L

        for (char in input) {
            value += char.code
            value *= 17
            value %= 256
        }

        return value
    }

    fun part1(input: List<String>): Long {
        return input[0].split(',').sumOf {
            hash(it)
        }
    }

    fun part2(input: List<String>): Long {
        val boxes = Array(256) { Box() }

        input[0].split(',')
            .forEach {
                if (it.endsWith("-")) {
                    val label = it.removeSuffix("-")
                    val hashValue = hash(label).toInt()
                    boxes[hashValue].removeIfExist(label)
                } else {
                    val (label, value) = it.split('=')
                    val hashValue = hash(label).toInt()
                    boxes[hashValue].append(label, value.toInt())
                }
            }
        return boxes.mapIndexed { i, b ->
            b.lensList.mapIndexed { j, (_, l) -> (i + 1L) * (j + 1L) * l }.sum()
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320L)

    val input = readInput("Day15")
    part1(input).println()

    check(part2(testInput) == 145L)

    part2(input).println()
}
