package day01

import java.lang.Math.abs

fun readInput(): List<String> =
    String.javaClass.getResource("/day01/input")
        .readText()
        .lines()
        .filter { it.isNotBlank() }

fun parse(input: List<String>): Pair<List<Int>, List<Int>> = input.asSequence()
    .map { it.split("   ") }
    .map { it[0].toInt() to it[1].toInt() }
    .unzip()
    .let { it.first.sorted() to it.second.sorted() }

fun part1(input: List<String>): Int {
    val (first, second) = parse(input)

    return first
        .zip(second)
        .map { abs(it.first - it.second) }
        .sum()
}

fun part2(input: List<String>): Int {
    val (first, second) = parse(input)

    val secondGrouped = second.asSequence()
        .groupBy { it }
        .map { it.key to it.value.size }
        .toMap()

    return first.sorted()
        .associateWith {
            secondGrouped[it]
        }
        .map { it.key * (it.value ?: 0) }
        .sum()
}

fun main() {
    val input = readInput()

    println(part1(input))
    println(part2(input))
}