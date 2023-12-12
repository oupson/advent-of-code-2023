import kotlin.math.max

fun placeRemaining(
    firstForm: CharArray, firstFormIndex: Int, secondForm: List<Int>, secondFormIndex: Int
): Long {
    val block = getNextBlock(firstForm, firstFormIndex)
    return if (block == null) {
        if (secondForm.size == secondFormIndex) {
            0L
        } else {
            -1L
        }
    } else if (secondFormIndex >= secondForm.size) {
        -1L
    } else if (block.first == '?') {
        processUnknownBlock(block, secondForm, secondFormIndex, firstForm, firstFormIndex)
    } else if (block.first == '#') {
        if (block.second == secondForm[secondFormIndex]) {
            val next = placeRemaining(firstForm, firstFormIndex + block.second + 1, secondForm, secondFormIndex + 1)
            if (next != -1L) {
                1L + next
            } else {
                -1L
            }
        } else {
            val next = getNextBlock(firstForm, firstFormIndex + block.second)
            if (next?.first == '?' && block.second + next.second < secondForm[secondFormIndex]) {
                val next = placeRemaining(
                    firstForm, firstFormIndex + secondForm[secondFormIndex] + 1, secondForm, secondFormIndex
                )
                if (next != -1L) {
                    1L + next
                } else {
                    -1L
                }
            } else {
                -1L
            }
        }
    } else {
        placeRemaining(firstForm, firstFormIndex + block.second, secondForm, secondFormIndex)
    }
}

private fun processUnknownBlock(
    block: Pair<Char, Int>,
    secondForm: List<Int>,
    secondFormIndex: Int,
    firstForm: CharArray,
    firstFormIndex: Int
): Long {
    return if (block.second > secondForm[secondFormIndex]) {
        val next = placeRemaining(
            firstForm, firstFormIndex + secondForm[secondFormIndex] + 1, secondForm, secondFormIndex + 1
        )

        val resWith = if (next != -1L) {
            1L + next
        } else {
            -1L
        }

        val resWithout = placeRemaining(
            firstForm, firstFormIndex + 1, secondForm, secondFormIndex
        )

        if (resWith == -1L && resWithout == -1L) {
            -1L
        } else {
            max(resWith, 0) + max(resWithout, 0)
        }
    } else if (block.second == secondForm[secondFormIndex]) {
        val nextBlock = getNextBlock(firstForm, firstFormIndex + block.second)
        if (nextBlock == null) {
            if (secondForm.size == secondFormIndex + 1) {
                1L
            } else {
                -1L
            }
        } else if (nextBlock.first == '.') {
            placeRemaining(firstForm, firstFormIndex + block.second + nextBlock.second, secondForm, secondFormIndex + 1)
        } else {
            -1L
        }
    } else {
        val nextBlock = getNextBlock(firstForm, firstFormIndex + block.second)

        if (nextBlock?.first == '#') {
            if (nextBlock.second + block.second == secondForm[secondFormIndex]) {
                val next = placeRemaining(
                    firstForm, firstFormIndex + secondForm[secondFormIndex] + 1, secondForm, secondFormIndex + 1
                )

                val resWith = if (next != -1L) {
                    1L + next
                } else {
                    -1L
                }

                val resWithout = placeRemaining(
                    firstForm, firstFormIndex + 1, secondForm, secondFormIndex
                )

                return if (resWith == -1L && resWithout == -1L) {
                    -1L
                } else {
                    max(resWith, 0) + max(resWithout, 0)
                }
            } else {
                return placeRemaining(
                    firstForm, firstFormIndex + 1, secondForm, secondFormIndex
                )
            }
        } else {
            -1L
        }
    }
}

fun getNextBlock(form: CharArray, startIndex: Int): Pair<Char, Int>? {
    return if (startIndex < form.size) {
        val char = form[startIndex]
        var size = 1

        while (startIndex + size < form.size && form[startIndex + size] == char) {
            size += 1
        }

        Pair(char, size)
    } else {
        null
    }
}


fun main() {
    fun Sequence<String>.parseInput() = this.map {
        val (firstForm, secondForm) = it.split(" ")

        Pair(firstForm.toCharArray(), secondForm.split(",").map { nbr -> nbr.toInt() })
    }


    fun part1(input: List<String>): Long {
        return input.asSequence().parseInput().map { (firstForm, secondForm) ->


            placeRemaining(firstForm, 0, secondForm, 0)
        }.sum()
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    check(part1(listOf("?###???????? 3,2,1")) == 10L)


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21L)


    val input = readInput("Day12")
    part1(input).println()

    check(part2(testInput) == 0L)

    part2(input).println()
}
