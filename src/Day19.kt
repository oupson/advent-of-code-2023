typealias Piece = MutableMap<String, Int>

data class Condition(
    val label: String, val isSup: Boolean, val value: Int
)

class Workflow(
    val defaultEnd: String, val acceptingRules: List<Pair<Condition, String>>
) {
    fun getEndLabel(piece: Piece): String {
        return acceptingRules.firstOrNull {
            if (it.first.isSup) {
                piece[it.first.label]!! > it.first.value
            } else {
                piece[it.first.label]!! < it.first.value
            }
        }?.second ?: defaultEnd
    }
}

fun Iterator<String>.parseWorkflows(): MutableMap<String, Workflow> {
    val regex = Regex("(\\S+)\\{(.+),(\\S+)}")
    val ruleRegex = Regex("(\\S)([<>])(\\d+):(\\S+)")
    val workflows = mutableMapOf<String, Workflow>()

    var line = this.next()
    while (line.isNotEmpty()) {
        val (_, name, rules, default) = regex.find(line)!!.groupValues
        workflows[name] = Workflow(default, rules.split(",").map {
            val (_, label, cond, value, endLabel) = ruleRegex.find(it)!!.groupValues

            Pair(Condition(label, cond == ">", value.toInt()), endLabel)
        })
        line = this.next()
    }

    return workflows
}

fun main() {
    fun part1(input: List<String>): Long {
        val inputStream = input.iterator()
        val workflows = inputStream.parseWorkflows()

        var res = 0L

        val partRegex = Regex("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}")
        for (line in inputStream) {
            val (_, x, m, a, s) = partRegex.find(line)!!.groupValues
            val piece = mutableMapOf("x" to x.toInt(), "m" to m.toInt(), "a" to a.toInt(), "s" to s.toInt())

            var currentWorkflow = "in"

            while (!(currentWorkflow == "A" || currentWorkflow == "R")) {
                currentWorkflow = workflows[currentWorkflow]!!.getEndLabel(piece)
            }

            if (currentWorkflow == "A") {
                res += piece.values.sum()
            }
        }

        return res
    }

    fun MutableMap<String, Workflow>.acceptedPossibilities(label: String): Long {
        return when (label) {
            "A" -> 1L
            "R" -> 0L
            else -> {
                var result = 1L
                var nonAccepted = 0

                for (rules in this[label]!!.acceptingRules) {
                    if (rules.first.isSup) {
                        nonAccepted += rules.first.value
                        result = (4000 - rules.first.value) * acceptedPossibilities(rules.second)
                    } else {
                        nonAccepted += 4000 - rules.first.value
                        result = rules.first.value * acceptedPossibilities(rules.second)
                    }
                }

                result += nonAccepted * acceptedPossibilities(this[label]!!.defaultEnd)

                result
            }
        }
    }

    fun part2(input: List<String>): Long {
        val inputStream = input.iterator()
        val workflows = inputStream.parseWorkflows()

        return workflows.acceptedPossibilities("in")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 19114L)

    val input = readInput("Day19")
    part1(input).println()

    check(part2(testInput) == 167409079868000L)

    part2(input).println()
}
