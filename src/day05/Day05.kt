package day05

import java.util.*

typealias OrderingRule = Pair<Int, Int>
typealias Update = List<Int>

fun readInput(): Pair<List<OrderingRule>, List<Update>> =
  String.javaClass.getResource("/day05/input")
    .readText()
    .lines()
    .filter { it.isNotBlank() }
    .partition { it.contains("|") }
    .let {
      it.first.map { it.split("|").map { it.toInt() }.let { it[0] to it[1] } } to
          it.second.map { it.split(",").map { it.toInt() } }
    }

fun isCorrect(update: Update, rules: List<OrderingRule>) = rules.none { rule ->
  update.contains(rule.first) && update.contains(rule.second) &&
      update.indexOf(rule.first) > update.indexOf(rule.second)
}

fun main() {
  val input = readInput()

  val (correct, incorrect) = input.second.partition { isCorrect(it, input.first) }

  correct.sumOf { it[it.size / 2] }.run(::println)

  val fixed = incorrect.map { update ->
    val corrected = update.toMutableList()
    val applicableRules = input.first.filter { rule -> update.containsAll(rule.toList()) }

    while (!isCorrect(corrected, applicableRules)) {

      applicableRules.forEach { rule ->
        val firstIndex = corrected.indexOf(rule.first)
        val secondIndex = corrected.indexOf(rule.second)
        if (secondIndex < firstIndex) {
          Collections.swap(corrected, secondIndex, firstIndex)
        }
      }
    }
    corrected
  }

  fixed.sumOf { it[it.size / 2] }.run(::println)
}

