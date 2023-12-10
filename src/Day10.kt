import kotlin.math.max
import kotlin.math.min

data class Tile(
    val x: Int,
    val y: Int,
    val connectedTiles: MutableSet<Tile>,
    var distance: Int? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tile

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "Tile(x=$x, y=$y, connectedTiles=${connectedTiles.size}, distance=$distance)"
    }
}

private fun parsePuzzleInput(input: List<String>): Pair<Tile, List<List<Tile>>> {
    val table = List(input.size) { y ->
        List(input[y].length) { x ->
            Tile(x, y, mutableSetOf())
        }
    }

    var startingTile: Tile? = null

    for (y in input.indices) {
        val tiles = input[y].toCharArray()
        for (x in tiles.indices) {
            val tile = table[y][x]
            val connectedTiles = when (tiles[x]) {
                '|' -> buildSet {
                    if (y > 0) {
                        add(table[y - 1][x])
                    }
                    if (y + 1 < table.size) {
                        add(table[y + 1][x])
                    }
                }

                '-' -> buildSet {
                    if (x > 0) {
                        add(table[y][x - 1])
                    }
                    if (x + 1 < tiles.size) {
                        add(table[y][x + 1])
                    }
                }

                'L' -> buildSet {
                    if (y > 0) {
                        add(table[y - 1][x])
                    }
                    if (x + 1 < tiles.size) {
                        add(table[y][x + 1])
                    }
                }

                'J' -> buildSet {
                    if (y > 0) {
                        add(table[y - 1][x])
                    }
                    if (x > 0) {
                        add(table[y][x - 1])
                    }
                }

                '7' -> buildSet {
                    if (y + 1 < table.size) {
                        add(table[y + 1][x])
                    }
                    if (x > 0) {
                        add(table[y][x - 1])
                    }
                }

                'F' -> buildSet {
                    if (y + 1 < table.size) {
                        add(table[y + 1][x])
                    }
                    if (x + 1 < tiles.size) {
                        add(table[y][x + 1])
                    }
                }

                'S' -> {
                    startingTile = tile
                    emptySet()
                }

                else -> emptySet()
            }

            tile.connectedTiles.addAll(connectedTiles)
        }
    }
    check(startingTile != null)

    fun Tile.addToStartingTileIfNeeded(startingTile: Tile) {
        if (this.connectedTiles.contains(startingTile)) {
            startingTile.connectedTiles.add(this)
        }
    }

    table.getOrNull(startingTile.y - 1)?.getOrNull(startingTile.x)?.addToStartingTileIfNeeded(startingTile)
    table.getOrNull(startingTile.y + 1)?.getOrNull(startingTile.x)?.addToStartingTileIfNeeded(startingTile)

    table.getOrNull(startingTile.y)?.getOrNull(startingTile.x - 1)?.addToStartingTileIfNeeded(startingTile)
    table.getOrNull(startingTile.y)?.getOrNull(startingTile.x + 1)?.addToStartingTileIfNeeded(startingTile)

    return Pair(startingTile, table)
}

fun fillGraph(tile: Tile) {
    val tilesToCheck = mutableListOf(tile)
    tile.distance = 0

    while (tilesToCheck.size > 0) {
        val tileToCheck = tilesToCheck.removeAt(0)
        val distance = (tileToCheck.distance!!)
        for (t in tileToCheck.connectedTiles) {
            if (t.distance == null || t.distance!! > distance + 1) {
                t.distance = distance + 1
                if (!tilesToCheck.contains(t)) {
                    tilesToCheck.add(t)
                }
            }
        }
    }
}

fun getLoop(startingTile: Tile, graph: List<List<Tile>>): Set<Tile> {
    fillGraph(startingTile)

    return graph.asSequence().flatMap { it.asSequence() }.filter { it.distance != null }.toSet()
}

fun main() {
    fun part1(input: List<String>): Int {
        val (startingTile, graph) = parsePuzzleInput(input)
        fillGraph(startingTile)
        return graph.asSequence().flatMap { it.asSequence() }.mapNotNull { it.distance }.max()
    }

    fun part2(input: List<String>): Int {
        val (startingTile, graph) = parsePuzzleInput(input)
        val loop = getLoop(startingTile, graph).toList()

        data class Wall(val isVertical: Boolean, val comp1: Int, val comp2: IntRange)

        val walls = mutableListOf<Wall>()

        var previousTile: Tile = startingTile
        var currentTile = previousTile.connectedTiles.first { it != previousTile }
        var isVertical = previousTile.x == currentTile.x
        var comp1 = if (isVertical) previousTile.x else previousTile.y
        var comp2 =
            Pair(if (isVertical) previousTile.y else previousTile.x, if (isVertical) currentTile.y else currentTile.x)

        while (currentTile != startingTile) {
            val nextTile = currentTile.connectedTiles.first { it != previousTile }
            previousTile = currentTile
            currentTile = nextTile

            if (isVertical && currentTile.x == comp1 || !isVertical && currentTile.y == comp1) {
                comp2 = Pair(comp2.first, if (isVertical) currentTile.y else currentTile.x)
            } else {
                walls.add(Wall(isVertical, comp1, min(comp2.first, comp2.second) until max(comp2.first, comp2.second)))
                isVertical = previousTile.x == currentTile.x
                comp1 = if (isVertical) previousTile.x else previousTile.y
                comp2 =
                    Pair(
                        if (isVertical) previousTile.y else previousTile.x,
                        if (isVertical) currentTile.y else currentTile.x
                    )
            }
        }

        walls.add(Wall(isVertical, comp1, min(comp2.first, comp2.second) until max(comp2.first, comp2.second)))

        val validGraph = graph.indices.map { y ->
            MutableList(graph[y].size) { x ->
                if (loop.any { it.x == x && it.y == y }) {
                    false
                } else {
                    var hit = 0
                    for (ix in 0 until x) {
                        if (walls.any { it.isVertical && it.comp1 == ix && y in it.comp2 }) {
                            hit += 1
                        }
                    }

                    hit % 2 == 1
                }
            }
        }

        return validGraph.flatten().count { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 4)

    val input = readInput("Day10")
    part1(input).println()

    val testInput2 = readInput("Day10_test2")
    check(part2(testInput2) == 8)

    part2(input).println()
}
