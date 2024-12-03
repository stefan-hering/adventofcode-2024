package day03

fun readInput(): String =
  String.javaClass.getResource("/day03/input")
    .readText()

fun part1(input: String): Int
  = Regex("mul\\((\\d+),(\\d+)\\)").findAll(input).map { it.groupValues[1].toInt() * it.groupValues[2].toInt() }.sum()

fun part2(input: String): Int
    = Regex("(don't\\(\\)|do\\(\\)|mul\\((\\d+),(\\d+)\\))").findAll(input)
      .map {
        when {
          it.groupValues[1] == "do()" -> true
          it.groupValues[1] == "don't()" -> false
          else -> it.groupValues[2].toInt() * it.groupValues[3].toInt()
        }
      }
      .fold(true to 0) { acc, instruction ->
        when {
          instruction == true -> true to acc.second
          instruction == false -> false to acc.second
          acc.first && instruction is Int -> true to acc.second + instruction
          else -> acc
        }
      }.second

fun main() {
  val input = readInput()
  println(part1(input))
  println(part2(input))
}