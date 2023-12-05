import kotlin.streams.asStream

data class Mapper(
    val source: String, val dest: String, val mapper: List<DestMapper>
)

data class DestMapper(
    val range: LongRange, val destOffset: Long
)

fun parseSourceInput(input: Sequence<String>): Pair<List<Long>, List<Mapper>> {
    val seeds = input.first().removePrefix("seeds: ").split(" ").map { it.toLong() }
    val mappers = mutableListOf<Mapper>()

    var currentSourceDest: Pair<String, String>? = null
    var currentDestMapper = mutableListOf<DestMapper>()

    for (line in input.drop(1)) {
        if (line.isEmpty()) {
            if (currentSourceDest != null) {
                mappers.add(
                    Mapper(
                        currentSourceDest.first, currentSourceDest.second, currentDestMapper
                    )
                )

                currentSourceDest = null
                currentDestMapper = mutableListOf()
            }
        } else if (currentSourceDest == null) {
            check(line.endsWith("map:"))
            currentSourceDest = line.removeSuffix(" map:").split("-to-").let { Pair(it.first(), it.last()) }
        } else {
            val nbrLine = line.split(" ").map { it.toLong() }
            val offset = nbrLine[0] - nbrLine[1]
            val range = nbrLine[1] until (nbrLine[1] + nbrLine[2])
            currentDestMapper.add(DestMapper(range, offset))
        }
    }

    if (currentSourceDest != null) {
        mappers.add(
            Mapper(
                currentSourceDest.first, currentSourceDest.second, currentDestMapper
            )
        )
    }

    return Pair(seeds, mappers)
}

fun List<Mapper>.mapSeedToLocation(seed: Long): Long {
    var state = "seed"
    var value = seed

    while (state != "location") {
        val mapper = this.find { it.source == state }!!
        val destMapper = mapper.mapper.find { value in it.range }

        if (destMapper != null) {
            value += destMapper.destOffset
        }
        state = mapper.dest
    }

    return value
}

fun main() {
    fun part1(input: List<String>): Long {
        val (seeds, mappers) = parseSourceInput(input.asSequence())
        return seeds.minOf { mappers.mapSeedToLocation(it) }
    }

    fun part2(input: List<String>): Long {
        val (seeds, mappers) = parseSourceInput(input.asSequence())
        return seeds.asSequence().chunked(2).flatMap { it[0] until (it[0] + it[1]) }.asStream().parallel()
            .mapToLong { mappers.mapSeedToLocation(it) }.min().asLong
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)

    val input = readInput("Day05")
    part1(input).println()

    check(part2(testInput) == 46L)

    part2(input).println()
}
