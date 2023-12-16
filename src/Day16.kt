import kotlin.streams.asStream

sealed class EnergyTile(var energized: Boolean = false) {
    class EmptyTile(energized: Boolean = false) : EnergyTile(energized) {
        override fun clone() = EmptyTile(this.energized)
    }

    data class Mirror(val isLeft: Boolean) : EnergyTile() {
        override fun clone(): EnergyTile = Mirror(this.isLeft).also { it.energized = this.energized }
    }

    data class Splitter(val isVertical: Boolean) : EnergyTile() {
        override fun clone(): EnergyTile = Splitter(this.isVertical).also { it.energized = this.energized }
    }

    abstract fun clone(): EnergyTile
}

data class BeamState(
    var x: Int, var y: Int, var isHeadingVertical: Boolean, var direction: Int
)

fun List<List<EnergyTile>>.cloneGrid() = this.map { it.map { c -> c.clone() } }

fun main() {
    fun Sequence<String>.parseInput() = this.map {
        it.map { c ->
            when (c) {
                '\\' -> EnergyTile.Mirror(false)
                '/' -> EnergyTile.Mirror(true)
                '|' -> EnergyTile.Splitter(true)
                '-' -> EnergyTile.Splitter(false)
                else -> EnergyTile.EmptyTile()
            }
        }
    }.toList()

    fun List<List<EnergyTile>>.explore(firstBeam: BeamState) {
        val grid = this
        val width = grid[0].size
        val currentStates = mutableListOf(firstBeam)
        val visitedState = mutableSetOf<BeamState>()

        while (currentStates.size > 0) {
            var beamIndex = 0
            while (beamIndex < currentStates.size) {
                val beam = currentStates[beamIndex]

                if (beam.y in grid.indices && beam.x in 0 until width) {
                    grid[beam.y][beam.x].energized = true

                    when (val tile = grid[beam.y][beam.x]) {
                        is EnergyTile.Mirror -> {
                            if (tile.isLeft) {
                                beam.direction *= -1
                            }

                            beam.isHeadingVertical = !beam.isHeadingVertical

                            if (beam.isHeadingVertical) {
                                beam.y += beam.direction
                            } else {
                                beam.x += beam.direction
                            }
                        }

                        is EnergyTile.Splitter -> {
                            if (tile.isVertical == beam.isHeadingVertical) {
                                if (beam.isHeadingVertical) {
                                    beam.y += beam.direction
                                } else {
                                    beam.x += beam.direction
                                }
                            } else {
                                beam.isHeadingVertical = !beam.isHeadingVertical
                                beam.direction = 1

                                if (beam.isHeadingVertical) {
                                    beam.y += beam.direction
                                } else {
                                    beam.x += beam.direction
                                }

                                val newBeamState = beam.copy(direction = -1)
                                if (newBeamState.isHeadingVertical) {
                                    newBeamState.y += newBeamState.direction
                                } else {
                                    newBeamState.x += newBeamState.direction
                                }

                                if (newBeamState !in visitedState) {
                                    visitedState.add(newBeamState.copy())
                                    currentStates.add(newBeamState)
                                }
                            }
                        }

                        is EnergyTile.EmptyTile -> {
                            if (beam.isHeadingVertical) {
                                beam.y += beam.direction
                            } else {
                                beam.x += beam.direction
                            }
                        }
                    }

                    if (beam !in visitedState) {
                        visitedState.add(beam.copy())
                        beamIndex += 1
                    } else {
                        currentStates.removeAt(beamIndex)
                    }
                } else {
                    currentStates.removeAt(beamIndex)
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val grid = input.asSequence().parseInput()
        grid.explore(BeamState(0, 0, false, 1))
        return grid.flatten().count { it.energized }
    }

    fun part2(input: List<String>): Int {
        val grid = input.asSequence().parseInput()
        val height = grid.size
        val width = grid[0].size

        return sequence {
            for (i in 0 until height) {
                yield(Pair(BeamState(0, i, false, 1), grid.cloneGrid()))
                yield(Pair(BeamState(width - 1, i, false, -1), grid.cloneGrid()))
            }

            for (i in 0 until width) {
                yield(Pair(BeamState(i, 0, true, 1), grid.cloneGrid()))
                yield(Pair(BeamState(i, height - 1, true, -1), grid.cloneGrid()))
            }
        }.asStream().parallel().mapToInt { (firstState, grid) ->
            grid.explore(firstState)
            grid.flatten().count { it.energized }
        }.max().asInt
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46)

    val input = readInput("Day16")
    part1(input).println()

    check(part2(testInput) == 51)

    part2(input).println()
}
