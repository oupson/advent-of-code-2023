sealed interface Module {
    fun flow(input: Int): Pair<List<Module>, Int>?
}

data class ConjunctionModule(var inputModules: List<Module>, var outputModule: List<Module>) : Module {
    override fun flow(input: Int): Pair<List<Module>, Int> {
        return Pair(outputModule, -input)
    }
}


class FlipFlopModule(
    inputModules: List<Module>,
    var outputModules: List<Module>,
    var isOn: Boolean = false
) : Module {
    var inputModules = buildMap {
        for (module in inputModules) {
            put(module, -1)
        }
    }

    override fun flow(input: Int): Pair<List<Module>, Int>? {
        return if (input > 0) {
            null
        } else {
            this.isOn = !this.isOn
            return Pair(outputModules ?: listOf(), if (this.isOn) 1 else -1)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        for (line in input) {
            val (input, outputs) = line.split(" -> ")
            val outputList = outputs.split(", ")


        }
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 0)

    val input = readInput("Day20")
    part1(input).println()

    check(part2(testInput) == 0)

    part2(input).println()
}
