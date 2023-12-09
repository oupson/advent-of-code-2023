#!/usr/bin/env bash

day=$(date +%d)
if ! [ -e "Day${day}.kt" ]; then
  echo "Creating files"
  kt=$(cat <<EOF
fun main() {
    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 0)

    val input = readInput("Day$day")
    part1(input).println()

    check(part2(testInput) == 0)

    part2(input).println()
}
EOF
)
  touch "Day${day}_test.txt"
  touch "Day${day}.txt"
  echo "$kt" >> "Day${day}.kt"
fi