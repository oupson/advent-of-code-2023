data class Node(val label: String, var left: Node?, var right: Node?) {
    override fun toString(): String {
        return "Node(label='$label', left='${left?.label}', right='${right?.label}')"
    }

    fun search(predicate: (Node) -> Boolean): Boolean {
        val visited = mutableSetOf<Node>()

        fun solve(node: Node): Boolean {
            if (predicate(node)) {
                return true
            } else if (visited.contains(node)) {
                return false
            }

            visited.add(node)

            return solve(node.left!!) || solve(node.right!!)
        }

        return solve(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        return label == other.label
    }

    override fun hashCode(): Int {
        return label.hashCode()
    }
}

val lineRegex = Regex("(\\S+) = \\((\\S+), (\\S+)\\)")

fun extractNode(line: String): Triple<String, String, String> {
    val matches = lineRegex.find(line) ?: throw RuntimeException("Line \"$line\" does not match the regex")
    val nodeName = matches.groups[1]!!.value
    val nodeLeft = matches.groups[2]!!.value
    val nodeRight = matches.groups[3]!!.value

    return Triple(nodeName, nodeLeft, nodeRight)
}


fun parseInput(input: List<String>): Pair<String, Map<String, Node>> {
    val instructions = input[0]

    val graph = mutableMapOf<String, Node>()

    for (line in input.asSequence().drop(2)) {
        val (nodeName, nodeLeft, nodeRight) = extractNode(line)
        val node = graph.getOrPut(nodeName) { Node(nodeName, null, null) }
        node.left = graph.getOrPut(nodeLeft) { Node(nodeLeft, null, null) }
        node.right = graph.getOrPut(nodeRight) { Node(nodeRight, null, null) }
    }

    return Pair(instructions, graph)
}

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun main() {
    fun part1(input: List<String>): Long {
        val (instructions, graph) = parseInput(input)
        val instructionLength = instructions.length.toLong()
        var currentPosition = graph["AAA"]!!
        var nbrActions = 0L

        check(currentPosition.search { it.label == "ZZZ" }) {
            "No route to ZZZ"
        }

        while (currentPosition.label != "ZZZ") {
            currentPosition = if (instructions[(nbrActions % instructionLength).toInt()] == 'L') {
                currentPosition.left!!
            } else {
                currentPosition.right!!
            }
            nbrActions += 1
        }

        return nbrActions
    }


    fun part2(input: List<String>): Long {
        val (instructions, graph) = parseInput(input)
        val instructionLength = instructions.length.toLong()
        val currentPositions = graph.toList().mapNotNull { if (it.first.endsWith('A')) it.second else null }.toList()

        check(currentPositions.all { it.search { n -> n.label.endsWith('Z') } }) {
            "No route to Z"
        }

        return currentPositions.stream().parallel().map {
            var nbrActions = 0L
            var currentPosition = it
            while (!currentPosition.label.endsWith('Z')) {
                currentPosition = if (instructions[(nbrActions % instructionLength).toInt()] == 'L') {
                    currentPosition.left!!
                } else {
                    currentPosition.right!!
                }
                nbrActions += 1
            }
            nbrActions
        }.reduce { acc, i ->
            findLCM(acc, i)
        }.get()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 6L)

    val input = readInput("Day08")
    part1(input).println()

    val testInput2 = readInput("Day08_test2")
    check(part2(testInput2) == 6L)

    part2(input).println()
}
