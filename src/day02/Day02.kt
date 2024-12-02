package day02

import kotlin.math.abs

fun readInput(): List<String> =
    String.javaClass.getResource("/day02/input")
        .readText()
        .lines()
        .filter { it.isNotBlank() }

fun parseInput(input: List<String>)
    = input.map { it.split(" ").map { it.toInt() } }

fun matchAllPairs(values: List<Int>, comp: (Pair<Int,Int>) -> Boolean) =
    values.zipWithNext()
        .fold(true) { acc, pair ->
            if(acc) comp(pair) else false
        }

fun isValid(report: List<Int>) =
    (matchAllPairs(report) { pair -> pair.first < pair.second}
        || matchAllPairs(report) { pair -> pair.first > pair.second})
        && matchAllPairs(report) { pair -> abs(pair.first - pair.second) in (1..3) }

fun part1(input: List<List<Int>>): Int {
    return input.asSequence()
        .filter(::isValid)
        .count()
}

fun part2(input: List<List<Int>>): Int {
    return input.asSequence()
        .map { report ->
            listOf(report) + report.indices.map { index -> report.filterIndexed { index2, _ -> index != index2}}
        }
        .filter {
            it.any(::isValid)
        }
        .count()
}

fun main() {
    val input = parseInput(readInput())

    println(part1(input))
    println(part2(input))
}